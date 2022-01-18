package ui;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.MethodExpression;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.*;
import oracle.jbo.domain.Number;

public class ApproveClass {
    private RichOutputText ctfMainIdText;

    public String approveCtf() {
        // Add event code here...
        String amDef = "model.AM.LOGAM";
        String config = "LOGAMLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObject vo = am.findViewObject("CtfMainOpenVO");
        // Work with your appmodule and view object here
        Object o[] = {ctfMainIdText.getValue()};
        Key k = new Key(o);
        Row r = vo.getRow(k);
        
System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx="+r.getAttribute("CtfStage")+r.getAttribute("CtfMainId"));
        r.setAttribute("CtfStage", "A");
        
        am.getTransaction().commit();
        Configuration.releaseRootApplicationModule(am, true);
        
        
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();

          MethodExpression me = ef.createMethodExpression(elc, "#{bindings.Execute.execute}", Object.class, new Class[0]);
          me.invoke(elc, new Object[0]);

        
        
        
        return null;
    }

    public void setCtfMainIdText(RichOutputText ctfMainIdText) {
        this.ctfMainIdText = ctfMainIdText;
    }

    public RichOutputText getCtfMainIdText() {
        return ctfMainIdText;
    }
}
