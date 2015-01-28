package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.JCRValueWrapper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.tools.files.*;
import org.apache.commons.fileupload.disk.DiskFileItem;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.ValueFactory;
import javax.jcr.PropertyType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class CreateIdeaAction extends Action {
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {            
      String img = "end-image";
      List<String> listTitle = parameters.get("title");
      List<String> listDescr = parameters.get("description");
      List<String> listyoutube = parameters.get("link-to-video");
      List<String> listChallenge = parameters.get("challengename");      
      //List<String> listImage = parameters.get("end-image");
      
      //FileUpload fileUpload = (FileUpload) req.getAttribute(FileUpload.FILEUPLOAD_ATTRIBUTE);
      //DiskFileItem inputFile = fileUpload.getFileItems().get("end-image");      
      
      //InputStream stream = new ByteArrayInputStream(listImage.get(0).getBytes("UTF8"));
      //JCRNodeWrapper imageWrapper;///sites/electrodea/contents/challenges
      ///sites/electrodea/home/challenges/create-challenge/demo-challenge-1/pagecontent/ideas/custom-rows-960gs
      //pagecontent/ideas
      ///sites/electrodea/home/challenges/create-challenge/demo-challenge-1/pagecontent/ideas
      //JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaIdea");
      JCRNodeWrapper challengeNode = session.getNode("/sites/electrodea/contents/challenges/" + listChallenge.get(0));      
      
      JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/ideas");
      JCRNodeWrapper ideaNode = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaIdea");
      //imageWrapper = ideaNode.uploadFile(inputFile.getName(), inputFile.getInputStream(), inputFile.getContentType());
      //imageWrapper = ideaNode.uploadFile("jnt:resource", inputFile.getInputStream(), inputFile.getContentType());
      
      ideaNode.setProperty("jcr:title", listTitle.get(0));
      ideaNode.setProperty("jcr:description", listDescr.get(0));
      //imageWrapper = ideaNode.uploadFile(img, stream,"image/jpeg" );
      
      //get youtube id from youtube-link/url
      String youtubeUrl = listyoutube.get(0);      
	  if (youtubeUrl != null && youtubeUrl.trim().length() > 0
				/*&& youtubeUrl.startsWith("http")*/ ) {
			String expression = "^.*((youtu.be"
					+ "\\/)"
					+ "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
			
			CharSequence input = youtubeUrl;
			Pattern pattern = Pattern.compile(expression,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				String groupIndex1 = matcher.group(7);
				if (groupIndex1 != null && groupIndex1.length() == 11)
					ideaNode.setProperty("video", groupIndex1);
               else return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
        else return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
                  
      //ideaNode.setProperty("image", imageWrapper.getPath());
      
      String[] newIdeas;
      
      if (challengeNode.hasProperty("ideas")) {
        JCRPropertyWrapper ideaProperty = challengeNode.getProperty("ideas");
        JCRValueWrapper[] ideas = ideaProperty.getRealValues();
        int i = 0;
        newIdeas = new String[ideas.length+1];
        for (JCRValueWrapper idea : ideas) {
           newIdeas[i] = idea.getNode().getUUID();
           i++;
        }
        
        newIdeas[ideas.length] = ideaNode.getUUID();
        
      } else {
        newIdeas = new String[1];
        newIdeas[0] = ideaNode.getUUID();
      }
      
      
      
      challengeNode.setProperty("ideas", newIdeas, PropertyType.WEAKREFERENCE);
        
	  
      //JCRNodeWrapper challengeNode = session.getNode("/sites/electrodea/contents/challenges/" + listChallenge.get(0));
      //challengeNode.getProperty("ideas").addValue(jcrNodeWrapper);
      //ValueFactory valueFactory = session.getValueFactory();
      //challengeNode.setProperty("ideas", valueFactory.createValue(jcrNodeWrapper), PropertyType.WEAKREFERENCE);

     // if(challengeNode == null) return new ActionResult(HttpServletResponse.SC_FORBIDDEN); 
        
      //challengeNode.setProperty("jcr:title", "TitelChallengeIdea");
	 // challengeNode.setProperty("ideas", jcrNodeWrapper, PropertyType.WEAKREFERENCE);      
      session.save();
            
      String targetPath = "/sites/electrodea/contents/ideas/" + listTitle.get(0);
	  parameters.remove(Render.REDIRECT_TO);      
      return new ActionResult(HttpServletResponse.SC_OK, targetPath);        
    }
}