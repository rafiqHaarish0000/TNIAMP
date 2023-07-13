package com.farmwiseai.tniamp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.ParseException;
import android.util.Log;

import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DepartmentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchDeptLookup {
    static List<ComponentData> getAllComponentData;
    String jsonResponse;
    private static CommonFunction commonFunction;

    public static List<ComponentData> getLookupValues(Activity activity) {
        getAllComponentData = new ArrayList<>();
        commonFunction = new CommonFunction(activity);
        if (commonFunction.isNetworkAvailable() == true) {
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getAgriComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            getAllComponentData = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComponentData>> call, Throwable t) {

                    }
                });
            } catch (Exception ex) {
                Log.e("Exception", ex.getMessage());
            }
        }

        return getAllComponentData;
    }

    public static List<ComponentData> readDataFromFile(Context context, String FILE_NAME) {
        List<ComponentData> componentDataList = null;

        try {
            componentDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                ComponentData obj = gson.fromJson(str, ComponentData.class);
                componentDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }
         /*   StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            jsonResponse = stringBuilder.toString();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return componentDataList;
    }

    public static List<Sub_Basin_Data> readSubBasin(Context context, String FILE_NAME) {
        List<Sub_Basin_Data> subBasinDataList = null;

        try {
            subBasinDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                Sub_Basin_Data obj = gson.fromJson(str, Sub_Basin_Data.class);
                subBasinDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }
         /*   StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            jsonResponse = stringBuilder.toString();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return subBasinDataList;
    }

    public static List<BlockData> readBlockData(Context context, String FILE_NAME) {
        List<BlockData> blockDataList = null;

        try {
            blockDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                BlockData obj = gson.fromJson(str, BlockData.class);
                blockDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }
         /*   StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            jsonResponse = stringBuilder.toString();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return blockDataList;
    }

    public static List<DistrictData> readDistrictData(Context context, String FILE_NAME) {
        List<DistrictData> districtDataList = null;

        try {
            districtDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                DistrictData obj = gson.fromJson(str, DistrictData.class);
                districtDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return districtDataList;
    }

    public static List<VillageData> readVillageData(Context context, String FILE_NAME) {
        List<VillageData> villageDataList = null;

        try {
            villageDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                VillageData obj = gson.fromJson(str, VillageData.class);
                villageDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return villageDataList;
    }

    public static void setLookUpValues(Context context,String FILE_NAME) {
//do something with your ArrayList
        try {
           String  text="data";
            File file = new File(context.getAssets().toString(), "lookups.json");

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(text);
            osw.flush();
            osw.close();
            fos.close();

          /*  File file = new File(context.getFilesDir(),FILE_NAME);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();*/
           /* AssetManager mngr = context.getAssets();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            ObjectOutputStream oos =
                    new ObjectOutputStream(bufferedReader);
            oos.writeObject(componentDataList);
            oos.close();*/
        } catch (Exception e) {
Log.e("Exception",e.getMessage());
        }
    }

    public static List<DepartmentData> readDepartmentData(Context context, String FILE_NAME) {
        List<DepartmentData> departmentDataList = null;

        try {
            departmentDataList = new ArrayList<>();
            Gson gson = new Gson();
          /*  File file = new File(context.getFilesDir(), FILE_NAME);
            FileReader fileReader = new FileReader(file);*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILE_NAME)));
            JsonArray jsonArray = new JsonParser().parse(bufferedReader).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement str = jsonArray.get(i);
                DepartmentData obj = gson.fromJson(str, DepartmentData.class);
                departmentDataList.add(obj);
                System.out.println(obj);
                System.out.println(str);
                System.out.println("-------");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return departmentDataList;
    }
}