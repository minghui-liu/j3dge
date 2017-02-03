package com.minghuiliu.base.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by kevin on 1/31/17.
 */
public class ResourceLoader {
    public static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;

        try
        {
            shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
            String line;

            while((line = shaderReader.readLine()) != null)
            {
                shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }

    public static Mesh loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if(!ext.equals("obj"))
        {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }

        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        BufferedReader meshReader = null;

        try
        {
            meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));
            String line;

            while((line = meshReader.readLine()) != null)
            {
                String[] tokens = line.split(" ");
                tokens = Utils.removeEmptyStrings(tokens);

                if(tokens.length == 0 || tokens[0].equals("#"))
                    continue;
                else if(tokens[0].equals("v"))
                {
                    vertices.add(new Vector3f(Float.valueOf(tokens[1]),
                            Float.valueOf(tokens[2]),
                            Float.valueOf(tokens[3])));
                }
                else if(tokens[0].equals("f"))
                {
                    indices.add(Integer.parseInt(tokens[1]) - 1);
                    indices.add(Integer.parseInt(tokens[2]) - 1);
                    indices.add(Integer.parseInt(tokens[3]) - 1);
                }
            }

            meshReader.close();

            Mesh res = new Mesh();
            Vector3f[] vertexData = new Vector3f[vertices.size()];
            vertices.toArray(vertexData);

            Integer[] indexData = new Integer[indices.size()];
            indices.toArray(indexData);

            res.addVertices(vertexData, Utils.toIntArray(indexData));

            return res;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
