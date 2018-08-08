package common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GameServerStartAndStop implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

        GameServerManager gameServerMgr = (GameServerManager) arg0.getServletContext().getAttribute("gameservermgr");
        gameServerMgr.stop();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        GameServerManager gameServerMgr = GameServerManager.getInstance();
        arg0.getServletContext().setAttribute("gameservermgr", gameServerMgr);
        gameServerMgr.startUp();
    }
}
