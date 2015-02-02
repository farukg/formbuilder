package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.JCRValueWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import org.jahia.modules.formbuilder.helper.FormBuilderHelper;


public class DeleteIdeaAction extends Action {

	JCRTemplate jcrTemplate = null;
	public void setJcrTemplate(JCRTemplate jcrTemplate) {
		this.jcrTemplate = jcrTemplate;
	}


	@Override
	public ActionResult doExecute(HttpServletRequest req, final RenderContext renderContext, final Resource resource, JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {

		return (ActionResult)jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {

			public Object doInJCR(JCRSessionWrapper session) throws RepositoryException { 

				// --- TODO: This should be done more dynamically
				// 1. option:
				// final JCRNodeWrapper nodeSession = resource.getNode();
				// 2. option:
				// 3. option:
				//JCRNodeWrapper node = session.getNodeByUUID(resource.getNode().getIdentifier());

				final List<String> listChTitle = parameters.get("ideaTitle");

                try {
                  int rights = FormBuilderHelper.checkWritingRights(session, renderContext, "", listChTitle.get(0), FormBuilderHelper.DELETE_IDEA);
                  System.out.println("Rights:" + rights);
                  if (rights != FormBuilderHelper.RET_SUCCESS) {
                    //In this case the user has not the right to delete the idea
                    String errorPath = "/sites/electrodea/error";
                    parameters.remove(Render.REDIRECT_TO);
                    return new ActionResult(HttpServletResponse.SC_OK, errorPath); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong 
                  }
                } catch (RepositoryException e) {
                  String errorPath = "/sites/electrodea/error";
				  e.printStackTrace();       
                  parameters.remove(Render.REDIRECT_TO);
                  return new ActionResult(HttpServletResponse.SC_OK, errorPath); //TODO redirect to a path with a more convinient error message, since the return code indiactes what went wrong  
                }
                
				final JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/ideas/"
						+listChTitle.get(0));

				if(!nodeSession.getPrimaryNodeTypeName().equals("sysewl:electrodeaIdea")) {
                  	
                  	System.out.println("debugalex: nodeSession.getPrimaryNodeTypeName: " +nodeSession.getPrimaryNodeTypeName());
                  
					return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
				else {

					// --- Check wheter user of this session is the same as creation user of this idea?!
					/*if(!session.getUser().getUsername().equals(nodeSession.getCreationUser()) ) {
						return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					} else {*/
                  
                  	//Verlinkung zu challenges entfernen
                    if (nodeSession.hasProperty("challenges")) {
                      JCRPropertyWrapper challenges = nodeSession.getProperty("challenges");
                      for (JCRValueWrapper challenge: challenges.getRealValues()) {
                        System.out.println("Challengename:" + challenge.getNode().getProperty("j:nodename"));   
                        if (challenge.getNode().hasNode("ideas")) {
                          
                          System.out.println("Ideas before:" + challenge.getNode().getProperty("ideas").getRealValues());
                          String[] newIdeas = new String[challenge.getNode().getProperty("ideas").getRealValues().length];
                          int i=0;
                          
                          for (JCRValueWrapper idea: challenge.getNode().getProperty("ideas").getRealValues()) {
                            if (!idea.getNode().getProperty("j:nodename").equals(listChTitle.get(0))) {
                              newIdeas[i]=idea.getNode().getUUID();
                              i++;
                            }                            
                          }
                          System.out.println("Ideas after:" + newIdeas);
                        }
                          
                        		
                        }
                    }
                  
					//nodeSession.remove();
					//session.save();
                

					String targetPath = "/sites/electrodea/home/challenges";
                    parameters.remove(Render.REDIRECT_TO);
					return new ActionResult(HttpServletResponse.SC_OK, targetPath);
				}
			}
		});
	}
}