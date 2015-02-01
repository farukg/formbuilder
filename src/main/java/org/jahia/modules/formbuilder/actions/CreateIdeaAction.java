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

import javax.jcr.RepositoryException;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRTemplate;
import org.json.JSONException;
import org.slf4j.Logger;
import org.jahia.services.usermanager.JahiaUser;

import org.jahia.modules.formbuilder.helper.FormBuilderHelper;

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

	JCRTemplate jcrTemplate;
	//Später für die ausgabe im catch und so
	//private static Logger logger = org.slf4j.LoggerFactory.getLogger(RateContent.class);
	public void setJcrTemplate(JCRTemplate jcrTemplate) {
		this.jcrTemplate = jcrTemplate;
	}

	@Override
	public ActionResult doExecute(final HttpServletRequest req, final RenderContext renderContext, final Resource resource, final JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
		return (ActionResult) jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {
			public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

				String img = "image";
				final List<String> listTitle = parameters.get("title");
				final List<String> listDescr = parameters.get("description");
				final List<String> listyoutube = parameters.get("link-to-video");
				final List<String> listChallenge = parameters.get("challengename");      
				final List<String> listImage = parameters.get("end-image");      

				/*System.out.println("RIGHTS:" + FormBuilderHelper.checkWritingRights(session, renderContext, listChallenge.get(0), listTitle.get(0), FormBuilderHelper.CREATE_IDEA));
                System.out.println("RIGHTS:" + FormBuilderHelper.checkWritingRights(session, renderContext, listChallenge.get(0), listTitle.get(0), FormBuilderHelper.CREATE_IDEA));
                System.out.println("RIGHTS:" + FormBuilderHelper.checkWritingRights(session, renderContext, listChallenge.get(0), listTitle.get(0), FormBuilderHelper.CREATE_IDEA));
              	try {
                  if (FormBuilderHelper.checkWritingRights(session, renderContext, listChallenge.get(0), listTitle.get(0), FormBuilderHelper.CREATE_IDEA) != FormBuilderHelper.RET_SUCCESS) {
                	//In this case the user has not the right to create an idea
                	return new ActionResult(HttpServletResponse.SC_FORBIDDEN); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong 
              	  }
                } catch (RepositoryException e) {
                 	return new ActionResult(HttpServletResponse.SC_FORBIDDEN); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong  
                }*/
              
				JCRNodeWrapper challengeNode = session.getNode("/sites/electrodea/contents/challenges/" + listChallenge.get(0));      
				JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/ideas");
				JCRNodeWrapper ideaNode = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaIdea");

				//image alt
				//InputStream stream = new ByteArrayInputStream(listImage.get(0).getBytes("UTF-8"));
				//JCRNodeWrapper imageWrapper;
				//imageWrapper = ideaNode.uploadFile(img, stream,"image/jpeg" );

				//image neu
				/**   try{
      JCRNodeWrapper imageWrapper;
      FileUpload fileUpload = (FileUpload) req.getAttribute(FileUpload.FILEUPLOAD_ATTRIBUTE);
      DiskFileItem inputFile = fileUpload.getFileItems().get("end-image");       
      imageWrapper = ideaNode.uploadFile(inputFile.getName(), inputFile.getInputStream(), inputFile.getContentType());
      imageWrapper = ideaNode.uploadFile(inputFile.getName(), inputFile.getInputStream(), inputFile.getContentType());    
      ideaNode.setProperty("endImage", imageWrapper);
          }catch(Exception e){
             ideaNode.setProperty("jcr:title", "Fehler");
           }
				 */ 

				ideaNode.setProperty("jcr:title", listTitle.get(0));
				ideaNode.setProperty("jcr:description", listDescr.get(0));

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

				String[] newIdeas;

				// --- An den Autor dieses Stück Quellcodes: Nach dem Löschen einer Idea in einer Challenge
                // --- kann mann in dieser challenge keine ideas mehr erstellen.
                // --- Fehlermeldung: NullPtrException bei in der Schleife bei newIdeas[i]              
				if (challengeNode.hasProperty("ideas")) {
					JCRPropertyWrapper ideaProperty = challengeNode.getProperty("ideas");
					JCRValueWrapper[] ideas = ideaProperty.getRealValues();
					int i = 0;
					newIdeas = new String[ideas.length+1];
					for (JCRValueWrapper idea : ideas) {
						newIdeas[i] = idea.getNode().getUUID();
                      	System.out.println("debugalex: If has property ideas. Counter i: " +i+ " newIdeas[i]: " +newIdeas[i]);
                      	i++;
					}

					newIdeas[ideas.length] = ideaNode.getUUID();

				} else {
					newIdeas = new String[1];
					newIdeas[0] = ideaNode.getUUID();
                  
                  	System.out.println("debugalex: else has property ideas. newIdeas[0]: " +newIdeas[0]);
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
		}); 
	}
}