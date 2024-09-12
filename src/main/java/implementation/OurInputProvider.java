package implementation;


import org.example.InputProvider;

class OurInputProvider implements InputProvider {

    private final interpreter.InputProvider theirs;

    OurInputProvider(interpreter.InputProvider theirs){
        this.theirs = theirs;
    }

    @Override
    public String readInput(String var1){
        return theirs.input(var1);
    }
}
