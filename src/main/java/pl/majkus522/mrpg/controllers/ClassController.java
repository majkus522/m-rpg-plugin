package pl.majkus522.mrpg.controllers;

import pl.majkus522.mrpg.common.classes.data.ClassData;

import java.io.File;
import java.util.ArrayList;

public class ClassController
{
    public static ClassData getClassData(String clazz)
    {
        if (clazz == null)
            return new ClassData();
        return FilesController.readJsonFile("data/classes/" + clazz, ClassData.class);
    }

    public static ArrayList<ClassData> getClassEvolutions(String clazz)
    {
        ArrayList<ClassData> placeholder = new ArrayList<>();
        for (File file : FilesController.scanDir("data/classes/"))
        {
            ClassData data = FilesController.readJsonFile("data/classes/" + file.getName().replace(".json", ""), ClassData.class);
            System.out.println(data);
            if (data.evolution.equals(clazz))
                placeholder.add(data);
        }
        return placeholder;
    }
}