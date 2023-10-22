package pl.majkus522.mrpg.controllers;

import pl.majkus522.mrpg.common.classes.data.ClassData;

public class ClassController
{
    public static ClassData getClassData(String clazz)
    {
        if (clazz == null)
            return new ClassData();
        return FilesController.readJsonFile("data/classes/" + clazz, ClassData.class);
    }
}