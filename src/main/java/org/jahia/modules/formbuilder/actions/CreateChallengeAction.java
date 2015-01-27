package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRValueWrapper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPropertyWrapperImpl;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.services.usermanager.JahiaUser; 
//import javax.jcr.PropertyIterator;
//import javax.jcr.Property;
import javax.jcr.PropertyType;


import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRTemplate;
import org.json.JSONException;
import org.slf4j.Logger;

import javax.jcr.RepositoryException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class CreateChallengeAction extends Action {
  
  JCRTemplate jcrTemplate;
  //für spätere Ausgabe
    //private static Logger logger = org.slf4j.LoggerFactory.getLogger(RateContent.class);
    
    public void setJcrTemplate(JCRTemplate jcrTemplate) {
        this.jcrTemplate = jcrTemplate;
    }
  
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource,final JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
     return (ActionResult) jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {
     public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
      List<String> listTitle = parameters.get("title");
      List<String> listDescr = parameters.get("describe-your-challenge");
      List<String> listCompl = parameters.get("status"); 
      List<String> listVisibil = parameters.get("visibility");
       List<String> listVisibilBox = parameters.get("visibilitybox"); //Userlist for private challenges
      
      JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/challenges");
      
      JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaChallenge");

      jcrNodeWrapper.setProperty("jcr:title", listTitle.get(0));      
      jcrNodeWrapper.setProperty("body", listDescr.get(0));
       
       
       System.out.println("userTEST"); 
                        System.out.println("userTEST"); 
       System.out.println("userTEST"); 
       System.out.println("userTEST"); 
       System.out.println("userTEST"); 
       System.out.println("userTEST"); 
       System.out.println("userTEST"); 
        
                          
	  if(listVisibil.get(0).equals("private")){
         
         jcrNodeWrapper.setProperty("private",true);
         
         JCRNodeWrapper nodeSessionGroup = session.getNode("/sites/electrodea/contents/groups");
      	 JCRNodeWrapper jcrNodeWrapperGroup = nodeSessionGroup.addNode(listTitle.get(0), "sysewl:electrodeaGroup");
        
        if (listVisibilBox != null) {
          String[] nodeList = new String[listVisibilBox.size()];
        
          for (int i = 0; i < listVisibilBox.size(); i++) {
         	 JCRNodeWrapper user = session.getNode(listVisibilBox.get(i));
             nodeList[i] = user.getUUID();
           
           //jcrNodeWrapperGroup.addNode("users", "jnt:user");      
           //System.out.println("user  identifier:" + user.getIdentifier());
           //jcrNodeWrapperGroup.addNode("members" , "sysewl:electrodeaGroup", jcrNodeWrapperGroup.getUUID(), null, null, null, null);
         }
         jcrNodeWrapperGroup.setProperty("members", nodeList, PropertyType.WEAKREFERENCE);
        
         /*System.out.println("HasProperty: " + jcrNodeWrapperGroup.hasProperty("members"));
          for (JCRValueWrapper user: jcrNodeWrapperGroup.getProperty("members").getRealValues()) {
            		 System.out.println("user:" + user.getNode().getProperty("j:nodename"));

          }*/
        
      }
         
      }else{
         jcrNodeWrapper.setProperty("private",false);
      }
      
      
      if(listCompl.get(0).equals("completed")){
      jcrNodeWrapper.setProperty("completed", true);
      }else{
      jcrNodeWrapper.setProperty("completed", false);
      }
      
      
    //  JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/");
      //JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaChallenge");
      
      //jcrNodeWrapper.setProperty("ideas","http://electrodea.tk:8080/sites/electrodea/contents/challenges/test123/test1234.html");
     
      //username test
      //String username = nodeSession.getUser().getUsername());

      //create folder for user test (not working)
      //JCRNodeWrapper userFolder = nodeSession.addNode(username, "jnt:folder");
      
      session.save();
            
      String targetPath = "/sites/electrodea/home/challenges"; 
      parameters.remove(Render.REDIRECT_TO);
      
      return new ActionResult(HttpServletResponse.SC_OK, targetPath);         
     } 
   });
     
    }
}