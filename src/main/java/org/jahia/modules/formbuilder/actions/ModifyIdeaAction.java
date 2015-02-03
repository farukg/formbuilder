        //alt, weiß nicht, ob jemand das noch braucht       
        /**JCRNodeWrapper nodeSessionTitle = session.getNode("/sites/mySite/testmodify/pagecontent/modify-challenge/fieldsets/title/title");
        //JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode("Titel des Formulares", "jnt:inputText");
		JCRNodeWrapper nodeSessionDescription = session.getNode("/sites/mySite/testmodify/pagecontent/modify-challenge/fieldsets/title/description");	
      
		nodeSessionTitle.setProperty("defaultValue", list.get(0));
      	nodeSessionDescription.setProperty("defaultValue", list2.get(0));
      	//list = parameters.get("SYSEWLOurdescription");
       	//jcrNodeWrapper.setProperty("body", list2.get(0));
     	//list = parameters.get("youlink");a
      	//jcrNodeWrapper.setProperty("video",list3.get(0)); **/

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jahia.modules.formbuilder.helper.FormBuilderHelper;

import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;


import org.json.JSONException;
import org.slf4j.Logger;
import javax.jcr.RepositoryException;
import java.io.IOException;

public class ModifyIdeaAction extends Action {
JCRTemplate jcrTemplate;
//Später für die ausgabe im catch und so
//private static Logger logger = org.slf4j.LoggerFactory.getLogger(RateContent.class);
public void setJcrTemplate(JCRTemplate jcrTemplate) {
this.jcrTemplate = jcrTemplate;
}
@Override
public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, final Resource resource, JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
return (ActionResult) jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {
public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

  		//get idea that should be modified
  		//for testing
  		List<String> challengePath = parameters.get("idea-to-modify");
  		JCRNodeWrapper ideaNode = session.getNode(challengePath.get(0));	
  		//JCRNodeWrapper ideaNode = resource.getNode(); 
     	if (!ideaNode.isNodeType("sysewl:electrodeaIdea"))
       		return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          
  		//read parameters from form (title must not be changed!) 
    	List<String> listDesc = parameters.get("description");  
		List<String> listyoutube = parameters.get("link-to-video");
  
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
						if (groupIndex1 != null && groupIndex1.length() == 11) {
							ideaNode.setProperty("video", groupIndex1);
						}
						else return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					else return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
              	else return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
                  
        /**try {
            if (FormBuilderHelper.checkWritingRights(session, renderContext, list.get(0), "", FormBuilderHelper.MODIFY_IDEA) != FormBuilderHelper.RET_SUCCESS) {
            	//In this case the user has not the right to modify the idea
        		String errorPath = "/sites/electrodea/error";
                    parameters.remove(Render.REDIRECT_TO);
                    return new ActionResult(HttpServletResponse.SC_OK, errorPath); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong 
                  }
                } catch (RepositoryException e) {
                    String errorPath = "/sites/electrodea/error";
                  	parameters.remove(Render.REDIRECT_TO);
                  	return new ActionResult(HttpServletResponse.SC_OK, errorPath); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong  
                }**/
       
        		
        //put parameter values into content object
		ideaNode.setProperty("jcr:description",listDesc.get(0));  
  		       	
        session.save();
  		
  		String targetPath = ideaNode.getPath();
        parameters.remove(Render.REDIRECT_TO); 
        return new ActionResult(HttpServletResponse.SC_OK, targetPath);
  
		}
	});
	}
}

     	 
      