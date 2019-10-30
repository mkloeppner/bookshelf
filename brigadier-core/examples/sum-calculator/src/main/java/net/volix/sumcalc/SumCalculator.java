package net.volix.sumcalc;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import net.volix.brigadier.Brigadier;
import net.volix.brigadier.BrigadierAdapter;
import net.volix.brigadier.command.Command;
import net.volix.brigadier.command.CommandInstance;
import net.volix.brigadier.command.ExecutionResult;
import net.volix.brigadier.command.ResultHandler;
import net.volix.brigadier.context.CommandContext;
import net.volix.brigadier.parameter.ParameterSet;

/**
 * @author Tobias Büser
 */
public class SumCalculator {

    public static void main(String[] args){
        String commandLine = "", username = "";

        Scanner scanner = new Scanner(System.in);

        System.out.println("What's your name? ");
        username = scanner.nextLine();

        System.out.println("Enter your command: ");
        commandLine = scanner.nextLine();

        // we split the arguments, so that we know what the label is
        String[] commandArgs = commandLine.split(" ");
        if(commandArgs.length < 1) {
            System.out.println("Wrong input.");
            return;
        }

        // setting the adapter to our own adapter
        Brigadier.getInstance().setAdapter(new Adapter());

        // start to register our command and execute the registration
        Brigadier.getInstance().register(new SumCommand()).execute();

        // execute the command with the input of the user
        Brigadier.getInstance().executeCommand(username, commandArgs[0], Arrays.copyOfRange(commandArgs, 1, commandArgs.length));
    }

    public static class SumCommand {

        @Command(label = "sum", usage = "<num1> <num2>")
        public void sum(String user, Context context, ParameterSet parameter) {
            Optional<Integer> num1 = parameter.getInt(0);
            Optional<Integer> num2 = parameter.getInt(1);

            if(!num1.isPresent() || !num2.isPresent()) {
                System.out.println("Wrong input. (no number given)");
                return;
            }

            System.out.println("=> Result: " + (num1.get() + num2.get()));
        }

        @ResultHandler
        public void handleResult(String user, CommandInstance command, ExecutionResult<Integer> result) {
            System.out.println("Result of '" + user + "': " + result.getResult());
        }

    }

    private static class Context extends CommandContext<String> {
        public Context(String commandSource, CommandInstance command, ParameterSet parameter) {
            super(commandSource, command, parameter);
        }

        // no other methods
    }

    private static class Adapter extends BrigadierAdapter<String> {
        @Override
        public void handleRegister(String label, CommandInstance instance) {
            // we don't want to register the command somewhere else
            // ignore
        }

        @Override
        public boolean checkPermission(String commandSource, CommandInstance command) {
            // the user always has permission
            return true;
        }

        @Override
        public void runAsync(Runnable runnable) {
            // do nothing, we don't want to run something async
        }

        @Override
        public Class<String> getCommandSourceClass() {
            return String.class;
        }

        @Override
        public CommandContext<String> constructCommandContext(String commandSource, CommandInstance command, ParameterSet parameter) {
            return new Context(commandSource, command, parameter);
        }
    }

}
