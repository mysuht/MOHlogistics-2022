package ui;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;

public class ItemsClass {
    private RichInputDate processDate;
    private RichInputText openBal;

    public void setProcessDate(RichInputDate processDate) {
        
        this.processDate = processDate;
    }

    public RichInputDate getProcessDate() {
        return processDate;
    }

    public void setOpenBal(RichInputText openBal) {
        this.openBal = openBal;
    }

    public RichInputText getOpenBal() {
        return openBal;
    }
    
    
    public void initialOpenBal(){
        openBal.setValue(0);
        
    }
}
