package Units;

public class Trait {
    String traitName;
    String traitDescription;
    public Trait(String name, String description){
        this.traitName = name;
        this.traitDescription = description;
    }

    public String getTraitName() {
        return traitName;
    }
    public String getTraitDescription(){
        return  traitDescription;
    }

    public String toString(){
        return "Name: " + traitName + "\nDescription: " + traitDescription + "\n";
    }
}

