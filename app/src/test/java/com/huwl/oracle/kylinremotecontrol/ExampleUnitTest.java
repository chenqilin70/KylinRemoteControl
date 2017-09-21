package com.huwl.oracle.kylinremotecontrol;

import com.google.gson.Gson;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;
import com.huwl.oracle.kylinremotecontrol.beans.User;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Gson gson=new Gson();
        Terminal t=new Terminal("id","name","type");
        t.setUser(new User("sdf","sdfsd"));
        System.out.println(gson.toJson(t));

    }
}