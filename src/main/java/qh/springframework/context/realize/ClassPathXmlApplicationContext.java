package qh.springframework.context.realize;

import qh.springframework.context.realize.abstractrealize.AbstractXmlApplicationContext;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[]{configLocation});
    }


    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
