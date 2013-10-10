/**
 * 
 */
package jw.asmsupport.operators.numerical.crement;

import jw.asmsupport.Crementable;
import jw.asmsupport.block.ProgramBlock;

/**
 * @author 温斯群(Joe Wen)
 *
 */
public class AfterDecrement extends AbstractDecrement {
    
    protected AfterDecrement(ProgramBlock block, Crementable factor) {
        super(block, factor);
    }

    @Override
    public void after() {
        this.execute();
    }

    @Override
    public void before() {
    }
        
    


}