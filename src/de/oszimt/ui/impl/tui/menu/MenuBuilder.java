package de.oszimt.ui.impl.tui.menu;

/**
 * Created by m588 on 24.10.2014.
 */
public class MenuBuilder {
    private Menu actualState;

    public MenuBuilder(Menu actualState) {
        this.actualState = actualState;
    }

    public void setActualState(Menu actualState) {
        this.actualState = actualState;
    }

    public void buildMenu(){
            this.actualState.buildMenu();
    }


}
