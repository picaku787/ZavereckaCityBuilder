import model.Building;
import model.BuildingType;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {
    private ImageIcon createDummyIcon(String name) {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        ImageIcon icon = new ImageIcon(img);
        icon.setDescription(name);
        return icon;
    }

    @Test
    public void testBuildingLevelStartsAtOne() {
        Building house = new Building("House", 10, 2, 2, 1,
                5, 0, 1, 0, createDummyIcon("house"), BuildingType.HOUSE);
        assertEquals(1, house.getLevel());
    }

    @Test
    public void testUpgradeIncreasesLevel() {
        Building house = new Building("House", 10, 2, 2, 1,
                5, 0, 1, 0, createDummyIcon("house"), BuildingType.HOUSE);
        house.upgrade();
        assertEquals(2, house.getLevel());
    }

    @Test
    public void testUpgradeDoublesPopulationBonus() {
        Building house = new Building("House", 10, 2, 2, 1,
                4, 0, 0, 0, createDummyIcon("house"), BuildingType.HOUSE);
        house.upgrade();
        assertEquals(40, house.getPopulationBonus());
    }

    @Test
    public void testUpgradeDoesNotExceedMaxLevel() {
        Building house = new Building("House", 10, 2, 2, 1,
                4, 0, 0, 0, createDummyIcon("house"), BuildingType.HOUSE);
        house.upgrade();
        house.upgrade();
        house.upgrade();
        assertEquals(3, house.getLevel());
    }

    @Test
    public void testGetTypeReturnsCorrectType() {
        Building shop = new Building("Shop", 10, 2, 2, 1,
                0, 0, 1, 4, createDummyIcon("house"), BuildingType.SHOP);
        assertEquals(BuildingType.SHOP, shop.getType());
    }
}
