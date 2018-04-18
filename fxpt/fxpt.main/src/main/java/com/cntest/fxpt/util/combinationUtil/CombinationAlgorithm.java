package com.cntest.fxpt.util.combinationUtil;

import java.math.BigInteger;

/**
 * 穷举策略接口
 * @date   01/29/2018
 */
public interface CombinationAlgorithm {

    public int getMaxSupportedSize();

    public BigInteger getCombinationCount(int numberOfElements, int numberToPick);

    public void fetchCombination(Object[] source, Object[] target, BigInteger ordinal);
}



