package ECS.Components;

import ECS.Component;

public class SpringRenderer extends Component
{

    private boolean b_firstTime = false;

    @Override
    public void update(float dt)
    {
        System.out.println("I am updating");
    }

    @Override
    public void start()
    {
        if(!b_firstTime)
        {
            System.out.println("I am starting");
            this.b_firstTime = true;
        }
    }
}
