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

public class ModifyChallengeAction extends Action {
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

  		//get challenge that should be modified
  		//for testing
  		List<String> challengePath = parameters.get("challenge-to-modify");
  		JCRNodeWrapper challenge = session.getNode(challengePath.get(0));	
  		//JCRNodeWrapper challenge = resource.getNode(); 
     	if (!challenge.isNodeType("sysewl:electrodeaChallenge"))
       		return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          
  		//read parameters from form (title must not be changed!) 
    	List<String> listDesc = parameters.get("describe-your-challenge");
  		List<String> listCompl = parameters.get("status"); 
		List<String> listVisibil = parameters.get("visibility");
          
        /**try {
            if (FormBuilderHelper.checkWritingRights(session, renderContext, list.get(0), "", FormBuilderHelper.MODIFY_CHALLENGE) != FormBuilderHelper.RET_SUCCESS) {
            	//In this case the user has not the right to modify the challenge
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
		challenge.setProperty("body",listDesc.get(0));  
  		if(listVisibil.get(0).equals("private")){
			challenge.setProperty("private",true);
    	}
 		 else {
    		challenge.setProperty("private",false);
 		}
  		if(listCompl.get(0).equals("completed")){
					challenge.setProperty("completed", true);
				}else{
					challenge.setProperty("completed", false);
				}
             	 
       	
        session.save();
  		
  		String targetPath = challenge.getPath();
        parameters.remove(Render.REDIRECT_TO); 
        return new ActionResult(HttpServletResponse.SC_OK, targetPath);
  
		}
	});
	}
}
