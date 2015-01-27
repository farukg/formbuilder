/**
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *     Copyright (C) 2002-2014 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     "This program is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation; either version 2
 *     of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 *     As a special exception to the terms and conditions of version 2.0 of
 *     the GPL (or any later version), you may redistribute this Program in connection
 *     with Free/Libre and Open Source Software ("FLOSS") applications as described
 *     in Jahia's FLOSS exception. You should have received a copy of the text
 *     describing the FLOSS exception, also available here:
 *     http://www.jahia.com/license"
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 *
 *
 * ==========================================================================================
 * =                                   ABOUT JAHIA                                          =
 * ==========================================================================================
 *
 *     Rooted in Open Source CMS, Jahia’s Digital Industrialization paradigm is about
 *     streamlining Enterprise digital projects across channels to truly control
 *     time-to-market and TCO, project after project.
 *     Putting an end to “the Tunnel effect”, the Jahia Studio enables IT and
 *     marketing teams to collaboratively and iteratively build cutting-edge
 *     online business solutions.
 *     These, in turn, are securely and easily deployed as modules and apps,
 *     reusable across any digital projects, thanks to the Jahia Private App Store Software.
 *     Each solution provided by Jahia stems from this overarching vision:
 *     Digital Factory, Workspace Factory, Portal Factory and eCommerce Factory.
 *     Founded in 2002 and headquartered in Geneva, Switzerland,
 *     Jahia Solutions Group has its North American headquarters in Washington DC,
 *     with offices in Chicago, Toronto and throughout Europe.
 *     Jahia counts hundreds of global brands and governmental organizations
 *     among its loyal customers, in more than 20 countries across the globe.
 *
 *     For more information, please visit http://www.jahia.com
 */
package org.jahia.modules.formbuilder.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Render;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONException;
import org.slf4j.Logger;
import org.jahia.services.usermanager.JahiaUser;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: rincevent
 * Date: 9/14/11   ;
 * Time: 2:15 PM
 */
public class CreateChallengeAction extends Action {
    JCRTemplate jcrTemplate;
  //für spätere Ausgaben
    //private static Logger logger = org.slf4j.LoggerFactory.getLogger(RateContent.class);
    
    public void setJcrTemplate(JCRTemplate jcrTemplate) {
        this.jcrTemplate = jcrTemplate;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, final Resource resource, JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        return (ActionResult) jcrTemplate.doExecuteWithSystemSession(null,session.getWorkspace().getName(),session.getLocale(),new JCRCallback<Object>() {
            public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                List<String> listTitle = parameters.get("title");
      final List<String> listDescr = parameters.get("describe-your-challenge");
      final List<String> listCompl = parameters.get("status"); 
      final List<String> listVisibil = parameters.get("visibility");
      final List<String> listVisibilBox = parameters.get("visibilitybox"); //Userlist for private challenges
      
      JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/challenges");
      
      JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaChallenge");

      jcrNodeWrapper.setProperty("jcr:title", listTitle.get(0));      
      jcrNodeWrapper.setProperty("body", listDescr.get(0));
                          
	  if(listVisibil.get(0).equals("private")){
         
         jcrNodeWrapper.setProperty("private",true);
         
         JCRNodeWrapper nodeSessionGroup = session.getNode("/sites/electrodea/contents/groups");
      	 JCRNodeWrapper jcrNodeWrapperGroup = nodeSessionGroup.addNode(listTitle.get(0),"sysewl:electrodeaGroup");
        
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
      
      
      //JCRNodeWrapper nodeSession = session.getNode("/sites/electrodea/contents/");
      //JCRNodeWrapper jcrNodeWrapper = nodeSession.addNode(listTitle.get(0), "sysewl:electrodeaChallenge");
      
      //jcrNodeWrapper.setProperty("ideas","http://electrodea.tk:8080/sites/electrodea/contents/challenges/test123/test1234.html");
     
      //username test
      //String username = nodeSession.getUser().getUsername());

      //create folder for user test (not working)
      //JCRNodeWrapper userFolder = nodeSession.addNode(username, "jnt:folder");
      
      session.save();
            
      //String targetPath = "/sites/electrodea/home/challenges"; 
      String targetPath = "/sites/electrodea/contents/challenges/" +listTitle.get(0); 
      parameters.remove(Render.REDIRECT_TO);
      
      return new ActionResult(HttpServletResponse.SC_OK, targetPath);         
     
            }
        });
    }
}
