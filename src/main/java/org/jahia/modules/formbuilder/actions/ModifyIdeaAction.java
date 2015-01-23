package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * User: toto
 * Date: 11/8/11
 * Time: 3:29 PM
 */
public class ModifyIdeaAction extends Action {
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
      
        JCRNodeWrapper node = resource.getNode();
      
	  	List<String> list = parameters.get("title");
    	List<String> list2 = parameters.get("description");
     	//List<String> list3 = parameters.get("youlink");
     	// List<String> list4 = parameters.get("sysEWLOURname");
      
   
        JCRNodeWrapper nodeSessionTitle = session.getNode("/sites/mySite/testmodify/pagecontent/modify-challenge/fieldsets/title/title");
        //JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode("Titel des Formulares", "jnt:inputText");
		JCRNodeWrapper nodeSessionDescription = session.getNode("/sites/mySite/testmodify/pagecontent/modify-challenge/fieldsets/title/description");	
      
		nodeSessionTitle.setProperty("defaultValue", list.get(0));
      	nodeSessionDescription.setProperty("defaultValue", list2.get(0));
      	//list = parameters.get("SYSEWLOurdescription");
       	//jcrNodeWrapper.setProperty("body", list2.get(0));
     	//list = parameters.get("youlink");a
      	//jcrNodeWrapper.setProperty("video",list3.get(0));
     	 
       	
        session.save();
        //return null;
  		String targetPath = "/sites/mySite/testmodify";
       parameters.remove(Render.REDIRECT_TO);
      
      
       
        return new ActionResult(HttpServletResponse.SC_OK, targetPath);
    }
 
    
    }