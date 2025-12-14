package world.lov;

import character.Hero;
import world.mh.*;
import world.lov.renderer.AsciiBoardRenderer;

import java.util.List;

public class LoVBoard {

    /* ================= DIFFICULTY ================= */

    public enum Difficulty {
        EASY(6),
        MEDIUM(4),
        HARD(2);

        private final int spawnInterval;

        Difficulty(int spawnInterval) {
            this.spawnInterval = spawnInterval;
        }

        public int getSpawnInterval() {
            return spawnInterval;
        }
    }

    /* ================= CONSTANTS ================= */

    public static final int SIZE = 8;

    // Lane layout: 0 1 |2| 3 4 |5| 6 7
    private static final int[] LANE_START_COLS = {0, 3, 6};
    private static final int[] INACCESSIBLE_COLS = {2, 5};

    /* ================= STATE ================= */

    private final Tile[][] grid;
    private final List<Hero> heroes;
    private final Difficulty difficulty;

    private final int[] heroRow, heroCol;
    private final int[] monsterRow, monsterCol;

    private final AsciiBoardRenderer renderer;

    private int lastCollisionLane = -1;
    private int roundCounter = 0;

    /* ================= CONSTRUCTOR ================= */

    public LoVBoard(List<Hero> heroes, Difficulty difficulty) {
        this.heroes = heroes;
        this.difficulty = difficulty;

        int n = heroes.size();
        heroRow = new int[n];
        heroCol = new int[n];
        monsterRow = new int[n];
        monsterCol = new int[n];

        grid = new Tile[SIZE][SIZE];
        renderer = new AsciiBoardRenderer(this);

        initBoard();
        spawnHeroes();
        spawnMonsters();
    }

    /* ================= INITIALIZATION ================= */

    private void initBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                if (isInaccessibleColumn(c)) {
                    grid[r][c] = new InaccessibleTile();
                    continue;
                }

                if (r == SIZE - 1) {
                    grid[r][c] = new NexusTile(NexusTile.Owner.HERO);
                    continue;
                }

                if (r == 0) {
                    grid[r][c] = new NexusTile(NexusTile.Owner.MONSTER);
                    continue;
                }

                grid[r][c] = LoVTileFactory.createPlayableTile();
            }
        }
    }

    private boolean isInaccessibleColumn(int c) {
        for (int x : INACCESSIBLE_COLS) {
            if (x == c) return true;
        }
        return false;
    }

    private void spawnHeroes() {
        for (int i = 0; i < heroes.size(); i++) {
            heroRow[i] = SIZE - 1;
            heroCol[i] = LANE_START_COLS[i];
        }
    }

    private void spawnMonsters() {
        for (int i = 0; i < heroes.size(); i++) {
            monsterRow[i] = 0;
            monsterCol[i] = LANE_START_COLS[i];
        }
    }

    /* ================= HERO MOVEMENT ================= */

    public WorldEvent moveHero(int i, Direction d) {

        int oldR = heroRow[i];
        int oldC = heroCol[i];

        int nr = oldR + d.dr;
        int nc = oldC;

        if (!valid(nr, nc)) return WorldEvent.NONE;
        if (!grid[nr][nc].isAccessible()) return WorldEvent.NONE;

        grid[oldR][oldC].onExit(heroes.get(i));

        heroRow[i] = nr;
        heroCol[i] = nc;

        grid[nr][nc].onEnter(heroes.get(i));

        if (nr == 0) return WorldEvent.HERO_WIN;
        if (collides(i)) return WorldEvent.BATTLE_TRIGGERED;

        return WorldEvent.NONE;
    }

    /* ================= MONSTER AI MOVEMENT ================= */

    public WorldEvent moveMonstersAI() {
        roundCounter++;

        for (int i = 0; i < monsterRow.length; i++) {

            int r = monsterRow[i];
            int c = monsterCol[i];
            int nr = r + 1;

            if (!valid(nr, c)) continue;
            if (!grid[nr][c].isAccessible()) continue;

            boolean occupied = false;
            for (int j = 0; j < monsterRow.length; j++) {
                if (monsterRow[j] == nr && monsterCol[j] == c) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) continue;

            monsterRow[i] = nr;

            if (nr == SIZE - 1) return WorldEvent.MONSTER_WIN;
            if (collides(i)) return WorldEvent.BATTLE_TRIGGERED;
        }

        // Difficulty-based respawn
        if (roundCounter % difficulty.getSpawnInterval() == 0) {
            spawnMonsters();
        }

        return WorldEvent.NONE;
    }

    /* ================= COLLISION ================= */

    private boolean collides(int i) {
        if (heroRow[i] == monsterRow[i] && heroCol[i] == monsterCol[i]) {
            lastCollisionLane = i;
            return true;
        }
        return false;
    }

    public int getLastCollisionLane() {
        return lastCollisionLane;
    }

    public void removeMonsterAtLastCollision() {
        if (lastCollisionLane >= 0 && lastCollisionLane < monsterRow.length) {
            monsterRow[lastCollisionLane] = -1;
            monsterCol[lastCollisionLane] = -1;
            lastCollisionLane = -1;
        }
    }

    /* ================= TELEPORT ================= */

    public enum TeleportResult { SUCCESS, INVALID }

    public TeleportResult teleportHero(int from, int to, int rowOffset) {

        if (from == to) return TeleportResult.INVALID;

        int targetR = heroRow[to] + rowOffset;
        int targetC = heroCol[to];

        if (!valid(targetR, targetC)) return TeleportResult.INVALID;
        if (!grid[targetR][targetC].isAccessible()) return TeleportResult.INVALID;

        for (int i = 0; i < heroes.size(); i++) {
            if (heroRow[i] == targetR && heroCol[i] == targetC)
                return TeleportResult.INVALID;
        }

        for (int i = 0; i < monsterRow.length; i++) {
            if (monsterCol[i] == targetC && monsterRow[i] > targetR)
                return TeleportResult.INVALID;
        }

        grid[heroRow[from]][heroCol[from]].onExit(heroes.get(from));
        heroRow[from] = targetR;
        heroCol[from] = targetC;
        grid[targetR][targetC].onEnter(heroes.get(from));

        return TeleportResult.SUCCESS;
    }

    /* ================= RECALL ================= */

    public enum RecallResult { SUCCESS, INVALID }

    public RecallResult recallHero(int i) {
        if (heroRow[i] == SIZE - 1) return RecallResult.INVALID;

        grid[heroRow[i]][heroCol[i]].onExit(heroes.get(i));
        heroRow[i] = SIZE - 1;
        heroCol[i] = LANE_START_COLS[i];

        return RecallResult.SUCCESS;
    }

    /* ================= RENDERING ================= */

    public char getOccupant(int r, int c) {
        for (int i = 0; i < heroes.size(); i++) {
            if (heroRow[i] == r && heroCol[i] == c) return 'H';
        }
        for (int i = 0; i < heroes.size(); i++) {
            if (monsterRow[i] == r && monsterCol[i] == c) return 'M';
        }
        return grid[r][c].getSymbol();
    }

    public void render() {
        renderer.render();
    }

    /* ================= API FIX ================= */

    public int getHeroCount() {
        return heroes.size();
    }

    private boolean valid(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }
}