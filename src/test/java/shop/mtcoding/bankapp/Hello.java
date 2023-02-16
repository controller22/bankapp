package shop.mtcoding.bankapp;

import org.junit.jupiter.api.Test;

public class Hello {

    @Test
    public void long_test() {
        Long n1 = 1270L;
        Long n2 = 1270L;
        System.out.println(n1 > n2);
        System.out.println(n1.longValue() == n2.longValue());
    }
}
