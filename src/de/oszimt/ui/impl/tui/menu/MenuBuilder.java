package de.oszimt.ui.impl.tui.menu;

import de.oszimt.concept.iface.IConcept;

/**
 * Created by m588 on 24.10.2014.
 */
public class MenuBuilder {
    private Menu actualState;
    private IConcept concept;

    public MenuBuilder(Menu actualState) {
        this.actualState = actualState;
    }

    public MenuBuilder(){
        this.actualState = new MainMenu(this);
    }

    public MenuBuilder(IConcept concept){
        this();
        this.concept = concept;
        this.actualState.setConcept(concept);
    }

    public void setActualState(Menu actualState) {
        this.actualState = actualState;
    }

    public void buildMenu(){
            this.actualState.buildMenu();
    }

    public IConcept getConcept() {
        return this.concept;
    }

}
