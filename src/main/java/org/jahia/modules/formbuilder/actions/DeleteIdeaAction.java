package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRNodeWrapper;
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


public class DeleteIdeaAction extends Action {

	JCRTemplate jcrTemplate = null;
	public void setJcrTemplate(JCRTemplate jcrTemplate) {
		this.jcrTemplate = jcrTemplate;
	}


	@Override
	public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, final Resource resource, JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {

		return (ActionResult)jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {

			public Object doInJCR(JCRSessionWrapper session) throws RepositoryException { 

				// --- TODO: This should be done more dynamically
				// 1. option:
				// final JCRNodeWrapper nodeSession = resource.getNode();
				// 2. option:
				// 3. option:
				//JCRNodeWrapper node = session.getNodeByUUID(resource.getNode().getIdentifier());

				final List<String> listChTitle = parameters.get("ideaTitle");
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
						nodeSession.remove();
						session.save();
                

					//String targetPath = "/sites/electrodea/home/challenges";
					return new ActionResult(HttpServletResponse.SC_OK);
				}
			}
		});
	}
}