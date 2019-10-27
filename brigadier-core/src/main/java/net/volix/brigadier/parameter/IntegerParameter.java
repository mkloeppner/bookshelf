package net.volix.brigadier.parameter;

import tv.rewinside.commons.java.string.Numerics;

/**
 * @author Tobias Büser
 */
public class IntegerParameter implements ParameterType<Integer> {

    @Override
    public Integer parse(final String string) {
        return Numerics.parseInt(string);
    }

    @Override
    public Class<Integer> getTypeClass() {
        return Integer.class;
    }

}
