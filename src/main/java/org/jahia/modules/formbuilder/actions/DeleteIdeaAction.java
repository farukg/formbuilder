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
public class DeleteIdeaAction extends Action {
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {    
      
      List<String> listChTitle = parameters.get("ideaTitle");
      
      // --- statically this works 
      JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/ideas/"
                                                   +listChTitle.get(0));
      
      //JCRNodeWrapper nodeSession = resource.getNode();
      
      if(nodeSession.getPrimaryNodeTypeName().equals("sysewl:electrodeaIdea")) {
        
        	nodeSession.remove();
          
      }
      else {
      		return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      
      session.save();
      
      
      // --- an idea how to realise a delete operation dynamically:
      // --- get current node
      //JCRNodeWrapper currentNode = resource.getNode();
      
      // --- some integrity tests before finally removing node:
      // --- check whether current node is of right type (addNode() in CreateIdeaAction sets this name)
      /*if (currentNode.getPrimaryNodeTypeName().equals("sysewl:electrodeaIdea")) {
        	currentNode.remove();
        
      }*/ 
      
      
      String targetPath = "/sites/electrodea/home/challenges";
      
      return new ActionResult(HttpServletResponse.SC_OK, targetPath);
    }
}