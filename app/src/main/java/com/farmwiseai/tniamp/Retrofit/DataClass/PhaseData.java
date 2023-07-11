package com.farmwiseai.tniamp.Retrofit.DataClass;

public class PhaseData {
    String name;

    public PhaseData(String name, String phaseValue) {
        this.name = name;
        PhaseValue = phaseValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhaseValue() {
        return PhaseValue;
    }

    public void setPhaseValue(String phaseValue) {
        PhaseValue = phaseValue;
    }

    String  PhaseValue;
}
