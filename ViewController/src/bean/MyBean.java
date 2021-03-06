package bean;

import java.util.List;

import javax.faces.component.UIComponent;

import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.RichMenu;
import oracle.adf.view.rich.component.rich.RichMenuBar;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandMenuItem;
import oracle.adf.view.rich.context.AdfFacesContext;
//
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import javax.sql.DataSource;

import java.io.Serializable;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;


public class MyBean {
    private RichMenu menuComponent;
    private RichButton button;
    private RichCommandMenuItem menuItem;
    private RichMenuBar parentMenuBarComponent;

    public MyBean() {
        
        AdfFacesContext adfContext = AdfFacesContext.getCurrentInstance();
        if (!adfContext.isPostback()){
                createDynamicMenu();
            }    
        
        
        
    }

    public void setMenuComponent(RichMenu menuComponent) {
        this.menuComponent = menuComponent;
    }

    public RichMenu getMenuComponent() {
        return menuComponent;
    }

    public void setButton(RichButton button) {
        this.button = button;
    }

    public RichButton getButton() {
        return button;
    }

    public void setMenuItem(RichCommandMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public RichCommandMenuItem getMenuItem() {
        return menuItem;
    }

    public void setParentMenuBarComponent(RichMenuBar parentMenuBarComponent) {
        this.parentMenuBarComponent = parentMenuBarComponent;
    }

    public RichMenuBar getParentMenuBarComponent() {
        return parentMenuBarComponent;
    }
    
    private void createDynamicMenu(){
    
//    List<UIComponent> myChildren =parentMenuBarComponent.getChildren();
    
    RichMenu menu1 =new RichMenu();
    menu1.setText("Перегляд");
    
    RichCommandMenuItem dept=new RichCommandMenuItem();
    dept.setText("Розділи");
    
    RichCommandMenuItem model=new RichCommandMenuItem();
    model.setText("Моделі");
        
    menu1.getChildren().add(dept);
    menu1.getChildren().add(model);
    
    if(parentMenuBarComponent==null){
            parentMenuBarComponent=new RichMenuBar(); 
    }
        
    parentMenuBarComponent.getChildren().add(menu1);
                
    
        
    }
    
    public static Connection getConnection(){
       try {
       InitialContext initialContex =new InitialContext();
       DataSource ds=(DataSource)  initialContex.lookup("school");  
       Connection conn=ds.getConnection();
       return conn;    
       } catch (Exception e) {
            e.printStackTrace();
        } 
       return null; 
    }
    private RichMenu createSubMenus(int mainMenuId,RichMenu menu){
    //
        Connection connection =null;
        PreparedStatement statement=null;
        ResultSet resultSet =null;
    //
    try {
        
        connection=getConnection();
        String sql="Select sub_menu_description,sub_menu_action from sub_main_menu where sub_menu_active='Y' "+
            "and parent_menu_id="+mainMenuId;
        statement=connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        
        while (resultSet.next()){
             String subMenuDesc=resultSet.getString(1);    
             String subMenuAction=resultSet.getString(2);    
             RichCommandMenuItem emp=new RichCommandMenuItem();
             emp.setText(subMenuDesc);
             MethodBinding mb= new ActionMethodBinding(subMenuAction);
             emp.setAction(mb);
             menu.getChildren().add(emp);
             }
        
    } catch (Exception e) {
         e.printStackTrace();
             
    } finally
           {
             try
             {
               resultSet.close();
               statement.close();
               connection.close();
             }
             catch (Exception e)
             {
               e.printStackTrace();
             }
           }
           return menu;
    }
    private void createMenus(){
//        
        Connection connection =null;
        PreparedStatement statement=null;
        ResultSet resultSet =null;
//        
       try {
           
           connection=getConnection();
           String sql="Select menu_id,menu_description from main_menu where menu_active='Y'" ;
           statement=connection.prepareStatement(sql);
           resultSet=statement.executeQuery();
           
           while (resultSet.next()){
                int menuId=resultSet.getInt(1);    
                String desc=resultSet.getString(2); 
                RichMenu menu=new RichMenu();
                menu.setText(desc);
                menu=createSubMenus(menuId,menu);
                parentMenuBarComponent.getChildren().add(menu);
               
               
               
               }
           
       } catch (Exception e) {
            e.printStackTrace();
        }        
        
        
    }
    
    private void createDynamicMenuStatic(){
    
    //    List<UIComponent> myChildren =parentMenuBarComponent.getChildren();
    
    RichMenu menu1 =new RichMenu();
    menu1.setText("Перегляд");
    
    RichCommandMenuItem dept=new RichCommandMenuItem();
    dept.setText("Розділи");
    
    RichCommandMenuItem model=new RichCommandMenuItem();
    model.setText("Моделі");
        
    menu1.getChildren().add(dept);
    menu1.getChildren().add(model);
    
    if(parentMenuBarComponent==null){
            parentMenuBarComponent=new RichMenuBar(); 
    }
        
    parentMenuBarComponent.getChildren().add(menu1);
                
    
        
    }
}
  class ActionMethodBinding extends MethodBinding implements  Serializable
  {
    private String result;

    public ActionMethodBinding(String result)
    {
      this.result = result;
    }

    public Object invoke(FacesContext context, Object params[])
    {
      return result;
    }

    public String getExpressionString()
    {
      return result;
    }

    public Class getType(FacesContext context)
    {
      return String.class;
    }
  }

