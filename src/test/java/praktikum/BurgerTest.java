package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockIngredient0;

    @Mock
    private Ingredient mockIngredient1;

    @Mock
    private Ingredient mockIngredient2;

    private float bunPrice;
    private float expectedPrice;
    private int ingredientCount;

    public BurgerTest(float bunPrice, float expectedPrice, int ingredientCount) {
        this.bunPrice = bunPrice;
        this.expectedPrice = expectedPrice;
        this.ingredientCount = ingredientCount;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();

        Mockito.when(mockBun.getPrice()).thenReturn(bunPrice);

        Mockito.when(mockIngredient0.getPrice()).thenReturn(50f);
        Mockito.when(mockIngredient1.getPrice()).thenReturn(50f);
        Mockito.when(mockIngredient2.getPrice()).thenReturn(50f);
    }

    @Test
    public void setBunsTest() {
        burger.setBuns(mockBun);
        assertEquals("Установлена неверная булочка", mockBun, burger.bun);
    }

    @Test
    public void addIngredientTest() {
        burger.addIngredient(mockIngredient0);
        assertEquals("Некорректное состояние списка ингредиентов после вызова метода addIngredient()", List.of(mockIngredient0), burger.ingredients);
    }

    @Test
    public void removeIngredientTest() {
        burger.addIngredient(mockIngredient0);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        assertEquals("После добавления трёх ингредиентов размер списка должен быть равен 3", 3, burger.ingredients.size());

        burger.removeIngredient(1);
        assertEquals("После удаления одного ингредиента (индекс 1) размер списка должен уменьшиться до 2", 2, burger.ingredients.size());
    }

    @Test
    public void moveIngredientTest() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        burger.moveIngredient(0, 1);
        assertEquals("Некорректное перемещение ингредиента между позициями", mockIngredient1, burger.ingredients.get(1));
    }

    @Parameterized.Parameters(name = "Цена булочки {0}, Ожидаемая итоговая цена {1}, Количество ингредиентов {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {100f, 200f, 0},
                {100f, 250f, 1},
                {100f, 300f, 2},
                {150f, 350f, 1},
                {200f, 550f, 3}
        });
    }

    @Test
    public void getPriceTest() {
        burger.setBuns(mockBun);

        if (ingredientCount >= 1) burger.addIngredient(mockIngredient0);
        if (ingredientCount >= 2) burger.addIngredient(mockIngredient1);
        if (ingredientCount >= 3) burger.addIngredient(mockIngredient2);

        assertEquals("Неверный расчет цены для булочки" + bunPrice + " и" + ingredientCount + " ингредиентов", expectedPrice, burger.getPrice(), 0.01f);
    }

    @Test
    public void getReceiptTest() {
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient0);
        burger.addIngredient(mockIngredient1);

        Mockito.when(mockBun.getName()).thenReturn("Space bun");
        Mockito.when(mockBun.getPrice()).thenReturn(100.0f);
        Mockito.when(mockIngredient0.getName()).thenReturn("Ingredient0");
        Mockito.when(mockIngredient1.getName()).thenReturn("Ingredient1");
        Mockito.when(mockIngredient0.getType()).thenReturn(IngredientType.FILLING);
        Mockito.when(mockIngredient1.getType()).thenReturn(IngredientType.SAUCE);
        Mockito.when(mockIngredient0.getPrice()).thenReturn(150.0f);
        Mockito.when(mockIngredient1.getPrice()).thenReturn(50.0f);

        String expectedReceipt = "(==== Space bun ====)\r\n" +
                "= filling Ingredient0 =\r\n" +
                "= sauce Ingredient1 =\r\n" +
                "(==== Space bun ====)\r\n" +
                "\r\n" +
                "Price: 400,000000\r\n";

        assertEquals("Ожидался чек:\n" + expectedReceipt + "\nБыл получен чек:\n" + burger.getReceipt(), expectedReceipt, burger.getReceipt());
    }

}
