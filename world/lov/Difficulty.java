package world.lov;

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