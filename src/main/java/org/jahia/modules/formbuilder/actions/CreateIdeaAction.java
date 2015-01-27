package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import java.io.*;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class CreateIdeaAction extends Action {
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {            
      String img = "endimage";
      List<String> listTitle = parameters.get("title");
      List<String> listDescr = parameters.get("description");
      List<String> listyoutube = parameters.get("link-to-video");
      List<String> listChallenge = parameters.get("challenge-name");
      //List<String> listImage = parameters.get("end-image");
      //InputStream stream = new ByteArrayInputStream(listImage.get(0).getBytes());
      //JCRNodeWrapper imageWrapper;/sites/electrodea/contents/challenges
      ///sites/electrodea/home/challenges/create-challenge/demo-challenge-1/pagecontent/ideas/custom-rows-960gs
      //pagecontent/ideas
      ///sites/electrodea/home/challenges/create-challenge/demo-challenge-1/pagecontent/ideas
      JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/challenges/" + listChallenge.get(0));      
      JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaIdea");
      
      jcrNodeWrapper.setProperty("jcr:title", listTitle.get(0));
      jcrNodeWrapper.setProperty("jcr:description", listDescr.get(0));
      //imageWrapper = jcrNodeWrapper.uploadFile(img, stream,"image/jpeg" );
      jcrNodeWrapper.setProperty("video", "yIZco8Dfyco"); // TODO: dynamically fetch YouTube-ID
	  //jcrNodeWrapper.setProperty("image", imageWrapper.getPath());
	  
     // JCRNodeWrapper challengeNode = session.getNode("/sites/electrodea/contents/challenges/" + listChallenge.get(0));
    //  challengeNode.getProperty("ideas").addValue(jcrNodeWrapper);

     // if(challengeNode == null) return new ActionResult(HttpServletResponse.SC_FORBIDDEN); 
        
      //challengeNode.setProperty("jcr:title", "TitelChallengeIdea");
	 // challengeNode.setProperty("ideas", jcrNodeWrapper, PropertyType.WEAKREFERENCE);      
      session.save();
            
      String targetPath = "";
		//parameters.remove(Render.REDIRECT_TO);      
      return new ActionResult(HttpServletResponse.SC_OK);        
    }
}