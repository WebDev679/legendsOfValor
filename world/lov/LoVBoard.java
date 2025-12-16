package world.lov;

import character.Hero;
import character.Monster;
import factory.MonsterFactory;
import world.mh.*;
import world.lov.renderer.AsciiBoardRenderer;
import ai.MonsterAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LoVBoard {

    public RecallResult recallHero(int i) {
        if (heroRow[i] == SIZE - 1) return RecallResult.INVALID;

        int lane = laneOfCol(heroCol[i]);

        grid[heroRow[i]][heroCol[i]].onExit(heroes.get(i));
        placeHeroAtNexus(i, lane);

        return RecallResult.SUCCESS;
    }


    public TeleportResult teleportHero(int from, int to, int rowOffset) {
        if (from == to) return TeleportResult.INVALID;

        int targetR = heroRow[to] + rowOffset;
        int targetC = heroCol[to];

        if (!tileAccessible(targetR, targetC)) return TeleportResult.INVALID;
        if (heroOccupies(targetR, targetC, from)) return TeleportResult.INVALID;

        // Cannot teleport behind a monster in the same column
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (ms.col == targetC && ms.row > targetR) {
                return TeleportResult.INVALID;
            }
        }

        grid[heroRow[from]][heroCol[from]].onExit(heroes.get(from));
        heroRow[from] = targetR;
        heroCol[from] = targetC;
        grid[targetR][targetC].onEnter(heroes.get(from));

        return TeleportResult.SUCCESS;
    }

    public boolean isHeroOnHeroNexus(Hero hero) {
        int idx = heroes.indexOf(hero);
        if (idx < 0) return false;

        int r = heroRow[idx];
        int c = heroCol[idx];

        if (!(grid[r][c] instanceof NexusTile)) return false;

        NexusTile tile = (NexusTile) grid[r][c];
        return tile.getOwner() == NexusTile.Owner.HERO;
    }

    public boolean isHeroOnMonsterNexus(Hero hero) {
        int idx = heroes.indexOf(hero);
        if (idx < 0) return false;

        int r = heroRow[idx];
        int c = heroCol[idx];

        if (!(grid[r][c] instanceof NexusTile)) return false;

        NexusTile tile = (NexusTile) grid[r][c];
        return tile.getOwner() == NexusTile.Owner.MONSTER;
    }

    public boolean anyMonsterOnHeroNexus() {
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;

            int r = ms.row;
            int c = ms.col;

            if (grid[r][c] instanceof NexusTile) {
                NexusTile tile = (NexusTile) grid[r][c];
                if (tile.getOwner() == NexusTile.Owner.HERO) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean anyHeroOnMonsterNexus() {
        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;

            if (isHeroOnMonsterNexus(hero)) return true;
        }
        return false;
    }

    public void advanceRound() {
        roundCounter++;
        if (roundCounter % difficulty.getSpawnInterval() == 0) {
            spawnMonsters();
        }
    }

    public enum TeleportResult { SUCCESS, INVALID }
    public enum RecallResult { SUCCESS, INVALID }

    public static int[] columnsInLane(int lane) {
        switch (lane) {
            case 0: return new int[]{0, 1};
            case 1: return new int[]{3, 4};
            case 2: return new int[]{6, 7};
            default: throw new IllegalArgumentException("Invalid lane: " + lane);
        }
    }

    public char getOccupant(int r, int c) {
        // Heroes take priority in rendering
        for (int i = 0; i < heroes.size(); i++) {
            if (!heroes.get(i).isAlive()) continue;
            if (heroRow[i] == r && heroCol[i] == c) {
                return 'H';
            }
        }

        // Then monsters
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (ms.row == r && ms.col == c) {
                return 'M';
            }
        }

        return grid[r][c].getSymbol();
    }
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
    private static final int[] INACCESSIBLE_COLS = {2, 5};

    private static final int[][] LANE_COLS = {
            {0, 1},
            {3, 4},
            {6, 7}
    };

    /* ================= INTERNAL STATE ================= */

    private static final class MonsterSlot {
        final Monster monster;
        int row;
        int col;

        MonsterSlot(Monster monster, int row, int col) {
            this.monster = monster;
            this.row = row;
            this.col = col;
        }
    }

    private final Tile[][] grid;
    private final List<Hero> heroes;
    private final Difficulty difficulty;
    private final Random rand;

    private final int[] heroRow;
    private final int[] heroCol;

    private final List<MonsterSlot> monsters = new ArrayList<>();
    private final AsciiBoardRenderer renderer;

    private int lastCollisionLane = -1;
    private int roundCounter = 0;
    private int[] laneSpineCol = new int[3];

    /* ================= CONSTRUCTOR ================= */

    public LoVBoard(List<Hero> heroes, Difficulty difficulty) {
        this(heroes, difficulty, new Random());
    }

    public LoVBoard(List<Hero> heroes, Difficulty difficulty, Random rand) {
        this.heroes = heroes;
        this.difficulty = difficulty;
        this.rand = rand;

        this.heroRow = new int[heroes.size()];
        this.heroCol = new int[heroes.size()];

        this.grid = new Tile[SIZE][SIZE];
        this.renderer = new AsciiBoardRenderer(this);
        for (int lane = 0; lane < 3; lane++) {
            int[] cols = colsInLane(lane);
            laneSpineCol[lane] = cols[rand.nextInt(2)];
        }

        initBoard();
        spawnHeroes();
        spawnMonstersInitial();
    }

    public List<Hero> getHeroes(){
        return Collections.unmodifiableList(this.heroes);
    }

    public List<Monster> getMonsters(){
        List<Monster> monsterList = new ArrayList<>();
        for (MonsterSlot slot: monsters) {
            monsterList.add(slot.monster);
        }
        return Collections.unmodifiableList(monsterList);
    }

    /* ================= BOARD INIT ================= */

    private void initBoard() {
        // Choose one guaranteed-open (spine) column per lane.
        int[] laneSpine = new int[3];
        for (int lane = 0; lane < 3; lane++) {
            int[] cols = colsInLane(lane);
            laneSpine[lane] = cols[rand.nextInt(2)];
        }
        // Choose exactly ONE obstacle row per lane (not nexus rows)
        int[] obstacleRowForLane = new int[3];
        for (int lane = 0; lane < 3; lane++) {
            obstacleRowForLane[lane] = 1 + rand.nextInt(SIZE - 2);
        }

        for (int r = 0; r < SIZE; r++) {
            // Hard walls stay hard walls.
            grid[r][2] = new InaccessibleTile();
            grid[r][5] = new InaccessibleTile();

            // Fill each lane.
            for (int lane = 0; lane < 3; lane++) {
                int[] cols = colsInLane(lane);
                int spineCol = laneSpine[lane];
                int otherCol = (cols[0] == spineCol) ? cols[1] : cols[0];

                // Nexus rows: no obstacles, ever.
                if (r == 0) {
                    grid[r][spineCol] = new NexusTile(NexusTile.Owner.MONSTER);
                    grid[r][otherCol] = new NexusTile(NexusTile.Owner.MONSTER);
                    continue;
                }
                if (r == SIZE - 1) {
                    grid[r][spineCol] = new NexusTile(NexusTile.Owner.HERO);
                    grid[r][otherCol] = new NexusTile(NexusTile.Owner.HERO);
                    continue;
                }

                // Middle rows: spine must be traversable (not obstacle).
                grid[r][spineCol] = LoVTileFactory.createNonObstaclePlayableTile();


                // Optionally block only the other column. Never block both.
                //boolean blockOther = rand.nextBoolean(); // tune probability if you want
                //grid[r][otherCol] = blockOther ? new ObstacleTile() : LoVTileFactory.createPlayableTile();
                if (r == obstacleRowForLane[lane]) {
                    grid[r][otherCol] = new ObstacleTile();
                } else {
                    grid[r][otherCol] = LoVTileFactory.createNonObstaclePlayableTile();
                }
            }
        }
    }





    private boolean isInaccessibleColumn(int c) {
        for (int x : INACCESSIBLE_COLS) {
            if (x == c) return true;
        }
        return false;
    }

    private static int laneOfCol(int col) {
        for (int lane = 0; lane < LANE_COLS.length; lane++) {
            for (int c : LANE_COLS[lane]) {
                if (c == col) return lane;
            }
        }
        throw new IllegalArgumentException("Column " + col + " is not part of any lane");
       // if (col <= 1) return 0;
        //if (col <= 4) return 1;
        //return 2;
    }

    private static int[] colsInLane(int lane) {
        return LANE_COLS[lane];
    }

    /* ================= SPAWNS ================= */

    private void spawnHeroes() {
        for (int i = 0; i < heroes.size(); i++) {
            placeHeroAtNexus(i, i % 3);
        }
    }

    private void placeHeroAtNexus(int heroIndex, int lane) {
        int row = SIZE - 1;
        int[] cols = colsInLane(lane);

        int chosen = cols[0];
        if (heroOccupies(row, cols[0], heroIndex)) {
            chosen = cols[1];
        } else if (!heroOccupies(row, cols[1], heroIndex)) {
            chosen = cols[rand.nextInt(2)];
        }

        heroRow[heroIndex] = row;
        heroCol[heroIndex] = chosen;
    }

    private void scaleMonsterStats(Monster m) {
        int heroCount = heroes.size();
        int roundFactor = Math.max(1, roundCounter / 5);

        double hpMultiplier =
                difficulty == Difficulty.HARD ? 3.0 :
                        difficulty == Difficulty.MEDIUM ? 2.2 : 1.8;

        hpMultiplier += 0.3 * heroCount;
        hpMultiplier += 0.2 * roundFactor;

        int newMaxHp = (int)(m.getMaxHp() * hpMultiplier);
        m.setMaxHp(newMaxHp);
        m.setHp(newMaxHp);
    }

    private void spawnMonstersInitial() {
        monsters.clear();

        int maxHeroLevel = heroes.stream().mapToInt(Hero::getLevel).max().orElse(1);

        for (int lane = 0; lane < Math.min(heroes.size(), 3); lane++) {
            Monster m = MonsterFactory.spawnSingle(maxHeroLevel);
            scaleMonsterStats(m);
            int col = colsInLane(lane)[rand.nextInt(2)];
            monsters.add(new MonsterSlot(m, 0, col));
        }
    }


    private void spawnMonsters() {
        int maxHeroLevel = heroes.stream().mapToInt(Hero::getLevel).max().orElse(1);

        for (int lane = 0; lane < Math.min(heroes.size(), 3); lane++) {
            if (laneHasAliveMonster(lane)) continue;

            Monster m = MonsterFactory.spawnSingle(maxHeroLevel);
            scaleMonsterStats(m);
            int col = colsInLane(lane)[rand.nextInt(2)];
            monsters.add(new MonsterSlot(m, 0, col));
        }    }

    private boolean laneHasAliveMonster(int lane) {
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (laneOfCol(ms.col) == lane) {
                return true;
            }
        }
        return false;
    }
    /* ================= MOVEMENT HELPERS ================= */

    private boolean valid(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private boolean tileAccessible(int r, int c) {
        return valid(r, c) && grid[r][c].isAccessible();
    }

    private boolean heroOccupies(int r, int c) {
        return heroOccupies(r, c, -1);
    }

    private boolean heroOccupies(int r, int c, int ignoreHero) {
        for (int i = 0; i < heroes.size(); i++) {
            if (i == ignoreHero) continue;
            if (!heroes.get(i).isAlive()) continue;
            if (heroRow[i] == r && heroCol[i] == c) return true;
        }
        return false;
    }

    private boolean monsterOccupies(int r, int c, MonsterSlot ignore) {
        for (MonsterSlot ms : monsters) {
            if (ms == ignore) continue;
            if (!ms.monster.isAlive()) continue;
            if (ms.row == r && ms.col == c) return true;
        }
        return false;
    }

    /* ===== Correct spec rule ===== */
    private boolean monsterAheadInLane(int heroRow, int lane) {
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (laneOfCol(ms.col) != lane) continue;
            if (heroRow - ms.row == 1) return true;
        }
        return false;
    }

    private boolean isObstacleAhead(int monsterR, int monsterC) {
        int nr = monsterR + 1;
        int nc = monsterC;

        if (!valid(nr, nc)) return true;
        return !grid[nr][nc].isAccessible();
    }

    private boolean heroDirectlyAhead(int monsterR, int monsterC) {
        int nextRow = monsterR + 1;
        int lane = laneOfCol(monsterC);

        for (int i = 0; i < heroes.size(); i++) {
            if (!heroes.get(i).isAlive()) continue;
            if (laneOfCol(heroCol[i]) != lane) continue;
            if (heroRow[i] == nextRow){
                System.out.println(heroRow[i] + " " + heroCol[i] + " " + monsterC);
                return true;
            }
        }
        return false;
    }

    private boolean heroMonsterAdjacent(int heroRow, int heroCol) {
        int lane = laneOfCol(heroCol);

        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (laneOfCol(ms.col) != lane) continue;

            if (Math.abs(ms.row - heroRow) == 1) {
                lastCollisionLane = lane;
                return true;
            }
        }
        return false;
    }

    private boolean laneCollisionAt(int r, int c) {
        int lane = laneOfCol(c);
        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;
            if (ms.row == r && laneOfCol(ms.col) == lane) {
                lastCollisionLane = lane;
                return true;
            }
        }
        return false;
    }

    /* ================= HERO MOVEMENT ================= */

    public WorldEvent moveHero(int i, Direction d) {
        int oldR = heroRow[i];
        int oldC = heroCol[i];

        int nr = oldR + d.dr;
        int nc = oldC + d.dc;

        if (!tileAccessible(nr, nc)) return WorldEvent.NONE;
        if (heroOccupies(nr, nc, i)) return WorldEvent.NONE;

        if (d.dr < 0) {
            int lane = laneOfCol(oldC);
            if (monsterAheadInLane(oldR, lane)) return WorldEvent.NONE;
        }

        grid[oldR][oldC].onExit(heroes.get(i));
        heroRow[i] = nr;
        heroCol[i] = nc;
        grid[nr][nc].onEnter(heroes.get(i));
        heroes.get(i).setPosition(new Position(nr, nc));

//        if (nr == 0) return WorldEvent.HERO_WIN;
        if(isHeroOnMonsterNexus(heroes.get(i))){
            return WorldEvent.HERO_WIN;
        }
        if (heroMonsterAdjacent(nr, nc)) return WorldEvent.BATTLE_TRIGGERED;
//        if (laneCollisionAt(nr, nc)) return WorldEvent.BATTLE_TRIGGERED;

        return WorldEvent.NONE;
    }

    private boolean monsterAdjacentToAnyHero(int monsterRow, int monsterCol) {
        int lane = laneOfCol(monsterCol);

        for (int i = 0; i < heroes.size(); i++) {
            if (!heroes.get(i).isAlive()) continue;
            if (laneOfCol(heroCol[i]) != lane) continue;

            if (Math.abs(heroRow[i] - monsterRow) == 1) {
                lastCollisionLane = lane;
                return true;
            }
        }
        return false;
    }

    /* ================= MONSTER MOVEMENT ================= */

    public WorldEvent moveMonstersAI(MonsterAI ai) {
        roundCounter++;

        for (MonsterSlot ms : monsters) {
            if (!ms.monster.isAlive()) continue;

            int lane = laneOfCol(ms.col);
            boolean forwardBlocked = heroDirectlyAhead(ms.row, ms.col) || isObstacleAhead(ms.row, ms.col);

            MonsterMove move = ai.decideMove(
                    ms.monster, ms.row, ms.col, lane, forwardBlocked
            );

            if (move == null) continue;

            int nr = move.newRow;
            int nc = move.newColumn;

            if (!valid(nr, nc)){
                continue;
            }
            if (!grid[nr][nc].isAccessible()) {
                continue;
            }
            if (laneOfCol(nc) != lane){
                continue;
            }
            if (heroOccupies(nr, nc)){
                continue;
            };
            if (monsterOccupies(nr, nc, ms)){
                continue;
            }
            if (nr > ms.row && forwardBlocked){
                continue;
            }

            ms.row = nr;
            ms.col = nc;

            ms.monster.setPosition(new Position(nr, nc));


            if (monsterAdjacentToAnyHero(nr, nc)) {
                return WorldEvent.BATTLE_TRIGGERED;
            }

            if (anyMonsterOnHeroNexus()){
                return WorldEvent.MONSTER_WIN;
            }
        }

        if (roundCounter % difficulty.getSpawnInterval() == 0) {
            spawnMonsters();
        }

        return WorldEvent.NONE;
    }

    public List<Hero> getHeroesInBattleLane() {
        if (lastCollisionLane < 0) return Collections.emptyList();

        List<Hero> result = new ArrayList<>();
        for (int i = 0; i < heroes.size(); i++) {
            if (!heroes.get(i).isAlive()) continue;
            if (laneOfCol(heroCol[i]) == lastCollisionLane) {
                result.add(heroes.get(i));
            }
        }
        return result;
    }

    /* ================= API ================= */

    public int getHeroCount() {
        return heroes.size();
    }

    public int getLastCollisionLane() {
        return lastCollisionLane;
    }

    public List<Monster> getMonstersInBattleLane() {
        if (lastCollisionLane < 0) return Collections.emptyList();

        List<Monster> result = new ArrayList<>();
        for (MonsterSlot ms : monsters) {
            if (ms.monster.isAlive() && laneOfCol(ms.col) == lastCollisionLane) {
                result.add(ms.monster);
            }
        }
        return result;
    }

    public void render() {
        renderer.render();
    }
}
