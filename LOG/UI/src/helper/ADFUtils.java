package helper;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCParameter;

import oracle.adf.share.logging.ADFLogger;

import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

import oracle.binding.ControlBinding;

import oracle.binding.OperationBinding;

//import oracle.fodemo.storefront.jsf.util.JSFUtils;

import oracle.jbo.ApplicationModule;
import oracle.jbo.AttributeDef;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * A series of convenience functions for dealing with ADF Bindings.
 * Note: Updated for JDeveloper 11
 *
 * @author Duncan Mills
 * @author Steve Muench
 *
 * $Id: ADFUtils.java 2513 2007-09-20 20:39:13Z ralsmith $.
 */
public class ADFUtils {

    public static final ADFLogger LOGGER =
        ADFLogger.createADFLogger(ADFUtils.class);

    /**
     * Get application module for an application module data control by name.
     * @param name application module data control name
     * @return ApplicationModule
     */
    public static ApplicationModule getApplicationModuleForDataControl(String name) {
        return (ApplicationModule) JSFUtils.resolveExpression("#{data." + name +
                                                             ".dataProvider}");
    }

    /**
     * A convenience method for getting the value of a bound attribute in the
     * current page context programatically.
     * @param attributeName of the bound value in the pageDef
     * @return value of the attribute
     */
    public static Object getBoundAttributeValue(String attributeName) {
        return findControlBinding(attributeName).getInputValue();
    }

    /**
     * A convenience method for setting the value of a bound attribute in the
     * context of the current page.
     * @param attributeName of the bound value in the pageDef
     * @param value to set
     */
    public static void setBoundAttributeValue(String attributeName,
                                              Object value) {
        findControlBinding(attributeName).setInputValue(value);
    }

    /**
     * Returns the evaluated value of a pageDef parameter.
     * @param pageDefName reference to the page definition file of the page with the parameter
     * @param parameterName name of the pagedef parameter
     * @return evaluated value of the parameter as a String
     */
    public static Object getPageDefParameterValue(String pageDefName,
                                                  String parameterName) {
        BindingContainer bindings = findBindingContainer(pageDefName);
        DCParameter param =
            ((DCBindingContainer)bindings).findParameter(parameterName);
        return param.getValue();
    }

    /**
     * Convenience method to find a DCControlBinding as an AttributeBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param bindingContainer binding container
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(BindingContainer bindingContainer,
                                                      String attributeName) {
        if (attributeName != null) {
            if (bindingContainer != null) {
                ControlBinding ctrlBinding =
                    bindingContainer.getControlBinding(attributeName);
                if (ctrlBinding instanceof AttributeBinding) {
                    return (AttributeBinding)ctrlBinding;
                }
            }
        }
        return null;
    }

    /**
     * Convenience method to find a DCControlBinding as a JUCtrlValueBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(String attributeName) {
        return findControlBinding(getBindingContainer(), attributeName);
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        return (BindingContainer) JSFUtils.resolveExpression("#{bindings}");
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer)getBindingContainer();
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName name of the value attribute to use
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List selectItemsForIterator(String iteratorName,
                                              String valueAttrName,
                                              String displayAttrName) {
        return selectItemsForIterator(findIterator(iteratorName),
                                      valueAttrName, displayAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with description.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName name of the value attribute to use
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute to use for description
     * @return ADF Faces SelectItem for an iterator binding with description
     */
    public static List selectItemsForIterator(String iteratorName,
                                              String valueAttrName,
                                              String displayAttrName,
                                              String descriptionAttrName) {
        return selectItemsForIterator(findIterator(iteratorName),
                                      valueAttrName, displayAttrName,
                                      descriptionAttrName);
    }

    /**
     * Get List of attribute values for an iterator.
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName value attribute to use
     * @return List of attribute values for an iterator
     */
    public static List attributeListForIterator(String iteratorName,
                                                String valueAttrName) {
        return attributeListForIterator(findIterator(iteratorName),
                                        valueAttrName);
    }

    /**
     * Get List of Key objects for rows in an iterator.
     * @param iteratorName iterabot binding name
     * @return List of Key objects for rows
     */
    public static List keyListForIterator(String iteratorName) {
        return keyListForIterator(findIterator(iteratorName));
    }

    /**
     * Get List of Key objects for rows in an iterator.
     * @param iter iterator binding
     * @return List of Key objects for rows
     */
    public static List keyListForIterator(DCIteratorBinding iter) {
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getKey());
        }
        return attributeList;
    }

    /**
     * Get List of Key objects for rows in an iterator using key attribute.
     * @param iteratorName iterator binding name
     * @param keyAttrName name of key attribute to use
     * @return List of Key objects for rows
     */
    public static List keyAttrListForIterator(String iteratorName,
                                              String keyAttrName) {
        return keyAttrListForIterator(findIterator(iteratorName), keyAttrName);
    }

    /**
     * Get List of Key objects for rows in an iterator using key attribute.
     *
     * @param iter iterator binding
     * @param keyAttrName name of key attribute to use
     * @return List of Key objects for rows
     */
    public static List keyAttrListForIterator(DCIteratorBinding iter,
                                              String keyAttrName) {
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(new Key(new Object[] { r.getAttribute(keyAttrName) }));
        }
        return attributeList;
    }

    /**
     * Get a List of attribute values for an iterator.
     *
     * @param iter iterator binding
     * @param valueAttrName name of value attribute to use
     * @return List of attribute values
     */
    public static List attributeListForIterator(DCIteratorBinding iter,
                                                String valueAttrName) {
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getAttribute(valueAttrName));
        }
        return attributeList;
    }

    /**
     * Find an iterator binding in the current binding container by name.
     *
     * @param name iterator binding name
     * @return iterator binding
     */
    public static DCIteratorBinding findIterator(String name) {
        DCIteratorBinding iter =
            getDCBindingContainer().findIteratorBinding(name);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + name + "' not found");
        }
        return iter;
    }

    /**
     * @param bindingContainer
     * @param iterator
     * @return
     */
    public static DCIteratorBinding findIterator(String bindingContainer,
                                                 String iterator) {
        DCBindingContainer bindings =
            (DCBindingContainer) JSFUtils.resolveExpression("#{" +
                                                           bindingContainer +
                                                           "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" +
                                       bindingContainer + "' not found");
        }
        DCIteratorBinding iter = bindings.findIteratorBinding(iterator);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + iterator +
                                       "' not found");
        }
        return iter;
    }

    /**
     * @param name
     * @return
     */
    public static JUCtrlValueBinding findCtrlBinding(String name) {
        JUCtrlValueBinding rowBinding =
            (JUCtrlValueBinding)getDCBindingContainer().findCtrlBinding(name);
        if (rowBinding == null) {
            throw new RuntimeException("CtrlBinding " + name + "' not found");
        }
        return rowBinding;
    }

    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param name operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String name) {
        OperationBinding op =
            getDCBindingContainer().getOperationBinding(name);
        if (op == null) {
            throw new RuntimeException("Operation '" + name + "' not found");
        }
        return op;
    }

    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param bindingContianer binding container name
     * @param opName operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String bindingContianer,
                                                 String opName) {
        DCBindingContainer bindings =
            (DCBindingContainer) JSFUtils.resolveExpression("#{" +
                                                           bindingContianer +
                                                           "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" +
                                       bindingContianer + "' not found");
        }
        OperationBinding op = bindings.getOperationBinding(opName);
        if (op == null) {
            throw new RuntimeException("Operation '" + opName + "' not found");
        }
        return op;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with description.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param valueAttrName name of value attribute to use for key
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with description
     */
    public static List selectItemsForIterator(DCIteratorBinding iter,
                                              String valueAttrName,
                                              String displayAttrName,
                                              String descriptionAttrName) {
        List selectItems = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName),
                                           (String)r.getAttribute(displayAttrName),
                                           (String)r.getAttribute(descriptionAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param valueAttrName name of value attribute to use for key
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List selectItemsForIterator(DCIteratorBinding iter,
                                              String valueAttrName,
                                              String displayAttrName) {
        List selectItems = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName),
                                           (String)r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List selectItemsByKeyForIterator(String iteratorName,
                                                   String displayAttrName) {
        return selectItemsByKeyForIterator(findIterator(iteratorName),
                                           displayAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with discription.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with discription
     */
    public static List selectItemsByKeyForIterator(String iteratorName,
                                                   String displayAttrName,
                                                   String descriptionAttrName) {
        return selectItemsByKeyForIterator(findIterator(iteratorName),
                                           displayAttrName,
                                           descriptionAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with discription.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with discription
     */
    public static List selectItemsByKeyForIterator(DCIteratorBinding iter,
                                                   String displayAttrName,
                                                   String descriptionAttrName) {
        List selectItems = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(),
                                           (String)r.getAttribute(displayAttrName),
                                           (String)r.getAttribute(descriptionAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return List of ADF Faces SelectItem for an iterator binding
     */
    public static List selectItemsByKeyForIterator(DCIteratorBinding iter,
                                                   String displayAttrName) {
        List selectItems = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(),
                                           (String)r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    /**
     * Find the BindingContainer for a page definition by name.
     *
     * Typically used to refer eagerly to page definition parameters. It is
     * not best practice to reference or set bindings in binding containers
     * that are not the one for the current page.
     *
     * @param pageDefName name of the page defintion XML file to use
     * @return BindingContainer ref for the named definition
     */
    private static BindingContainer findBindingContainer(String pageDefName) {
        BindingContext bctx = getDCBindingContainer().getBindingContext();
        BindingContainer foundContainer =
            bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }

    /**
     * @param opList
     */
    public static void printOperationBindingExceptions(List opList) {
        if (opList != null && !opList.isEmpty()) {
            for (Object error : opList) {
                LOGGER.severe(error.toString());
            }
        }
    }

    public static void addPartialTarget(UIComponent component) {
        RequestContext adfContext;

        adfContext = RequestContext.getCurrentInstance();
        adfContext.addPartialTarget(component);
    }

    public static void applyTableFilters(RichTable table) {
        FilterableQueryDescriptor queryDescriptor =

            (FilterableQueryDescriptor)table.getFilterModel();

        if (queryDescriptor != null &&
            queryDescriptor.getFilterCriteria() != null) {
            table.queueEvent(new QueryEvent(table, queryDescriptor));
            addPartialTarget(table);

        }
    }

    public static void clearTableFilters(RichTable table) {
        FilterableQueryDescriptor queryDescriptor =

            (FilterableQueryDescriptor)table.getFilterModel();

        if (queryDescriptor != null &&
            queryDescriptor.getFilterCriteria() != null) {
            queryDescriptor.getFilterCriteria().clear();
            table.queueEvent(new QueryEvent(table, queryDescriptor));
            addPartialTarget(table);

        }
    }


    public static void closePopUp(RichPopup popup) {
        popup.cancel();
    }

    public static void showPopUp(RichPopup popup) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        popup.show(hints);

    }

    /** * When a bounded task flow manages a transaction (marked as requires-transaction,. * requires-new-transaction, or requires-existing-transaction), then the * task flow must issue any commits or rollbacks that are needed. This is * essentially to keep the state of the transaction that the task flow understands * in synch with the state of the transaction in the ADFbc layer. * * Use this method to issue a commit in the middle of a task flow while staying * in the task flow. */
    public static void saveAndContinue() {
        Map sessionMap =
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        BindingContext context =
            (BindingContext)sessionMap.get(BindingContext.CONTEXT_ID);
        String currentFrameName = context.getCurrentDataControlFrame();
        DataControlFrame dcFrame =
            context.findDataControlFrame(currentFrameName);
        dcFrame.commit();
        dcFrame.beginTransaction(null);
    }

    /** * Programmatic evaluation of EL. * * @param el EL to evaluate * @return Result of the evaluation */
    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        return exp.getValue(elContext);
    }

    /** * Programmatic invocation of a method that an EL evaluates to. * The method must not take any parameters. * * @param el EL of the method to invoke * @return Object that the method returns */
    public static Object invokeEL(String el) {
        return invokeEL(el, new Class[0], new Object[0]);
    }

    /** * Programmatic invocation of a method that an EL evaluates to. * * @param el EL of the method to invoke * @param paramTypes Array of Class defining the types of the parameters * @param params Array of Object defining the values of the parametrs * @return Object that the method returns */
    public static Object invokeEL(String el, Class[] paramTypes,
                                  Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        MethodExpression exp =
            expressionFactory.createMethodExpression(elContext, el,
                                                     Object.class, paramTypes);
        return exp.invoke(elContext, params);
    }

    /** * Sets a value into an EL object. Provides similar functionality to * the <af:setActionListener> tag, except the from is * not an EL. You can get similar behavior by using the following… * setEL(to, evaluateEL(from)) * * @param el EL object to assign a value * @param val Value to assign */
    public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        exp.setValue(elContext, val);
    }
    
    public static void navigateToControlFlowCase(String controlFlowCase) {

        FacesContext fctx = FacesContext.getCurrentInstance();
        Application application = fctx.getApplication();
        NavigationHandler navHandler = application.getNavigationHandler();
        navHandler.handleNavigation(fctx, null, controlFlowCase);

    }


    public static void resetValueInputItems(AdfFacesContext adfFacesContext,
                                      UIComponent component) {
       if (component != null) {
            List<UIComponent> items = component.getChildren();
            for (UIComponent item : items) {
                if (item != null) {
                    resetValueInputItems(adfFacesContext, item);
                    if (item instanceof UIXEditableValue) {
                        UIXEditableValue input = (UIXEditableValue)item;
                        if (input != null) {
                            input.resetValue();
                            adfFacesContext.addPartialTarget(input);
                        }
                    }
                }
            }   
        }
    }
    
    public static void setFocus(UIComponent comp){
        if(comp != null){
          FacesContext context = FacesContext.getCurrentInstance();
          StringBuilder script = new StringBuilder();
          script.append("var comp = AdfPage.PAGE.findComponent('").append(comp.getClientId(context)).append("'); ").append("comp.focus();");

          ExtendedRenderKitService erks =
              Service.getService(context.getRenderKit(),
                                 ExtendedRenderKitService.class);
          erks.addScript(context, script.toString());
        }
        
   
}

    public static void customFilteringforTable(RichTable table, String bindingQuery) {


        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor)table.getFilterModel();

        if (queryDescriptor != null &&
            queryDescriptor.getFilterCriteria() != null) {
            Map originalMap = queryDescriptor.getFilterCriteria();

            for (Map.Entry<String, Object> entry :
                 queryDescriptor.getFilterCriteria().entrySet()) {

                if (!"".equals(entry.getValue())) {

                    originalMap.put(entry.getKey(),
                                    "%" + entry.getValue() + "%");
                }
            }
            QueryEvent queryEvent = new QueryEvent(table, queryDescriptor);
            queryEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
           // ELUtils.invokeMethod(bindingQuery, QueryEvent.class, queryEvent);

            for (Map.Entry<String, Object> entry :
                 queryDescriptor.getFilterCriteria().entrySet()) {
               
                if (!"".equals(entry.getValue())) {

                    originalMap.put(entry.getKey(),
                                    entry.getValue().toString().replaceAll("%",
                                                                           ""));
                }
            }
            ADFUtils.addPartialTarget(table);
        }
    }
    
    public static ViewObject getChildViewObjectFromParent(ViewObject parentVO, String childAccessorName) {
        AttributeDef attrDef = parentVO.findAttributeDef(childAccessorName);
        if(attrDef == null) {
          throw new RuntimeException("Attribute Definition '" + childAccessorName + "' not found on parent view object");
        }    
        return attrDef.getAccessorVO(parentVO);
    }
    
    public static void clearPageFlowScope(Object key){
        Map pageFlowScopeMap = AdfFacesContext.getCurrentInstance().getPageFlowScope();
        pageFlowScopeMap.remove(key);
    }
    
    public static void clearViewScope(Object key){
        Map viewScope = AdfFacesContext.getCurrentInstance().getViewScope();
        viewScope.remove(key);
    }
    
   
    
   
}



