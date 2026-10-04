package Units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TraitsTest {
    @Test
    public void getInstanceReturnsSameTraitsObject() {
        assertSame(Traits.getInstance(), Traits.getInstance());
    }

    @Test
    public void allTraitsCannotBeModifiedDirectly() {
        Traits traits = Traits.getInstance();

        assertThrows(UnsupportedOperationException.class,
                () -> traits.getAllTraits().put("Fearless", new Trait("Fearless", "Ignores morale penalties.")));
    }

    @Test
    public void getTraitReturnsTraitByName() {
        Traits traits = Traits.getInstance();

        Trait trait = traits.getTrait("Shield Wall");

        assertEquals("Shield Wall", trait.getTraitName());
    }

    @Test
    public void traitToStringIncludesNameAndDescription() {
        Trait trait = new Trait("Shield Wall", "This unit may use Shields Up Multiple times in a round.");

        String output =
                "Name: Shield Wall\n" +
                "Description: This unit may use Shields Up Multiple times in a round.\n";

        assertEquals(output, trait.toString());
    }
}
