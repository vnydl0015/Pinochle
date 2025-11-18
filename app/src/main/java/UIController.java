// Team Number: Wed-16:00-Team-03

// UIController controls GUI updates via GGButtons.
// It uses template pattern and inheritance.
public abstract class UIController {

    protected final GameSystem system;

    protected final ActorFactory factory;

    public UIController(GameSystem system, ActorFactory factory) {
        this.system = system;
        this.factory = factory;
    }

    // a simple and short steps
    void process() {
        create();
        askFor();
    }

    abstract void create();
    abstract void remove();
    abstract void add();
    abstract void askFor();
}
