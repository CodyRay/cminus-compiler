package io.github.haroldhues;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class EnumerableTest
{
    @Test
    public void testToList() throws Exception
    {
        StringSource testSource = new StringSource("Hello World!");
        List<Character> result = testSource.toList();
        assertThat(result, is(Arrays.asList('H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!')));
    }
}
