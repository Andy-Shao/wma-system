package com.andyshao.application.wma;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnabledIf(expression = "${integration.test}", loadContext = true)
public abstract class IntegrationTest {
}
