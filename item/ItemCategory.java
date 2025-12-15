package item;

/**
 * High level classification for items.
 *
 * <p>This enum lets higher level systems such as markets and menus
 * reason about what kind of item is being handled without depending
 * on concrete implementation classes. It also provides a convenient
 * extension point for adding new Legends of Valor specific items
 * (for example ARTIFACT) without changing existing code paths.</p>
 */
public enum ItemCategory {
    WEAPON,
    ARMOR,
    POTION,
    SPELL,
    /**
     * Placeholder for Legends of Valor specific items that do not
     * naturally fit into the core categories above.
     */
    ARTIFACT
}


