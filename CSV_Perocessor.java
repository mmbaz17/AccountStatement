package Deeds.person;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.Path;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class CSV_Processor {


	//windows
	private static  String CSV_FILE_PATH = "c:/app/"; 				// "c:/app/A0117163_85292.tdf";

	//linux
	//private static  String CSV_FILE_PATH = "/u01/app/oracle/wars/deedsweb/bulkprocess/"; 
	
    static ArrayList<CSV_Type_1> Lst1 = new ArrayList<CSV_Type_1>();
    static ArrayList<CSV_Type_2> Lst2 = new ArrayList<CSV_Type_2>();
    static ArrayList<CSV_Type_3> Lst3 = new ArrayList<CSV_Type_3>();
    static ArrayList<CSV_Type_4> Lst4 = new ArrayList<CSV_Type_4>();
    static ArrayList<CSV_Type_5> Lst5 = new ArrayList<CSV_Type_5>();
    static ArrayList<CSV_Type_6> Lst6 = new ArrayList<CSV_Type_6>();

    static List<String> fileList = new ArrayList<String>();
    private static  String OUTPUT_ZIP_FILE = "";
    private static  String SOURCE_FOLDER = "";
 

/*
    public static void main(String[] args) throws IOException {
       // try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
        		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
               // CSVParser csvRecords = new CSVParser(reader, CSVFormat.DEFAULT);
        
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                String ThisHeader = csvRecord.get(0);
                if (ThisHeader.trim().startsWith("RECORD TYPE")) {
                    continue;
                } else {
                    Aggregator(csvRecord);
                }
            }
            WriteFiles();
            DoZipper();
            csvParser.close();
            System.out.println("Exit - ");
       // }
      //  catch (Exception ex) {}
    }

    */
    @SuppressWarnings("unlikely-arg-type")
	public static String ProcessEDF(String fname) throws IOException {
        // try (
    	String retfile = "";
    	CSV_FILE_PATH = fname;
                 Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                 
                 ((BufferedReader) reader).readLine();

                 String line;
				while ((line = ((BufferedReader) reader).readLine()) != null) {
					
                     boolean data = line.contentEquals(";"); 
                     continue;

				}
                 
         		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
                // CSVParser csvRecords = new CSVParser(reader, CSVFormat.DEFAULT);
         		
 	
         			
         				
             for (CSVRecord csvRecord : csvParser) {
                 // Accessing Values by Column Index
                 String ThisHeader = csvRecord.get(0);
                 if (ThisHeader.trim().startsWith("RECORD TYPE")) {
                     continue;
                 } else {
                     Aggregator(csvRecord);
                 }
             }
         		
             WriteFiles();
             DoZipper();
             retfile = OUTPUT_ZIP_FILE;
             csvParser.close();
             System.out.println("Exit - ");
             return retfile;
        // }
       //  catch (Exception ex) {}
     }

    
    public static void Aggregator(CSVRecord csvRecord) {
        String type = csvRecord.get(0);
        if(type.contains(";")) {
        	type.replaceAll(";", ".");
        	
        } else
        {
        switch (type) {
            case "1":
                //System.out.println("Record No - " + csvRecord.getRecordNumber() + " >>> Type 1");
                CSV_Type_1 typ1 = CSV_Type_Fixer.SolidifyRecordType1(csvRecord, type);
                Lst1.add(typ1);
                break;
            case "2":
                CSV_Type_2 typ2 = CSV_Type_Fixer.SolidifyRecordType2(csvRecord, type);
                Lst2.add(typ2);
                break;
                
            case "3":
                CSV_Type_3 typ3 = CSV_Type_Fixer.SolidifyRecordType3(csvRecord, type);
                Lst3.add(typ3);
                break;
            case "4":
                CSV_Type_4 typ4 = CSV_Type_Fixer.SolidifyRecordType4(csvRecord, type);
                Lst4.add(typ4);
                break;
            case "5":
                CSV_Type_5 typ5 = CSV_Type_Fixer.SolidifyRecordType5(csvRecord, type);
                Lst5.add(typ5);
                break;
            case "6":
                CSV_Type_6 typ6 = CSV_Type_Fixer.SolidifyRecordType6(csvRecord, type);
                Lst6.add(typ6);
                break;
        }
      }
    }

    @SuppressWarnings("unchecked")
	public static void WriteFiles() throws IOException  {

        //String dirPath ="config/subDir";
       // final String CSV_FILE_PATH = "c:/app/";
        String fname = CSV_FILE_PATH.substring(0,CSV_FILE_PATH.lastIndexOf("."));
        String rxt = CSV_FILE_PATH.substring(CSV_FILE_PATH.lastIndexOf("."));
        String fname2 = fname.substring(fname.lastIndexOf("/")+1);
        //Windows
        String dirPath = "c:/app/" + fname2;
        
        //Linux
      // String dirPath = "/u01/app/oracle/wars/deedsweb/bulkprocess/" + fname2;
        
        SOURCE_FOLDER = dirPath;
        OUTPUT_ZIP_FILE = dirPath + "/"+ fname2+"_person" + ".zip" ;//+ rxt;
        // Check If Directory Already Exists Or Not?
        Path dirPathObj = Paths.get(dirPath);
        boolean dirExists = Files.exists(dirPathObj);
        if(dirExists) {
            System.out.println("! Directory Already Exists !");
        } else {
            try {
                // Creating The New Directory
                Files.createDirectories(dirPathObj);
            } catch (IOException ioExceptionObj) {
                System.out.println("Problem Occured While Creating The Directory = " + ioExceptionObj.getMessage());
            }
        }

          String outt = dirPath + "/" + "type1.csv";
        //createFileList(new File(outt));
        int num = 1;
       // try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outt));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("SERIAL","ERF NO",
                		"TOWNSHIP NAME","PORTION NO","RESTANT","ADDITIONAL DESC","DIAGRAM NO","EXTENT","PROVINCE"));
       // ) {
if(!Lst1.isEmpty()) {
            for (CSV_Type_1 obj: Lst1){
                csvPrinter.printRecord(Arrays.asList(num,obj.getErfNo(), obj.getTownshipName(),obj.getPortionNo(), obj.getRestant(),
                obj.getAddDescription(),obj.getDiagramNo(),obj.getExtent(),obj.getProvince()));
                num ++;
            }
}
else
{
	csvPrinter.printRecord ("No record found");
}

            csvPrinter.flush();
            csvPrinter.close();
       // }
       // catch (IOException ex){}

            //Process File 2
        outt = dirPath + "/" + "type2.csv";
        //createFileList(new File(outt));

           // try (
                    BufferedWriter writer2 = Files.newBufferedWriter(Paths.get(outt));

                    CSVPrinter  csvPrinter2 = new CSVPrinter(writer2, CSVFormat.DEFAULT
                            .withHeader("SERIAL", "ERF NO","TOWNSHIP NAME","PORTION NO","FARM NAME","REGISTRATION DIV","CLEARANCE",
                            		"SITUATED TOWNSHIP NAME","SITUATED ERF NO","SITUATED PRTN NO"));
                    
          //  ) {
                    if(!Lst2.isEmpty()) {
                 num = 1;
                for (CSV_Type_2 obj: Lst2){
                    csvPrinter2.printRecord(Arrays.asList(num, 
                    obj.getErfNo(),   
                    obj.getTownshipName(),
                    obj.getPortionNo(),
                    obj.getFarmName(),
                    obj.getRegDivision(),
                    obj.getClearance(),
                    obj.getSituatedTownshipName(),
                    obj.getSituatedErfNo(),
                    obj.getSituatedPortionNo()));
                    num ++;
                }
                    }
                    else
                    {
                    	csvPrinter2.printRecord ("No record found");
                    }
                csvPrinter2.flush();
                csvPrinter2.close();
           // }
           // catch (IOException ex){}
                //Process File 3
        outt = dirPath + "/" + "type3.csv";

              //  try (
                        BufferedWriter writer3 = Files.newBufferedWriter(Paths.get(outt));

                        CSVPrinter  csvPrinter3 = new CSVPrinter(writer3, CSVFormat.DEFAULT
                                .withHeader("SERIAL", "ERF NO","TOWNSHIP NAME","PORTION NO","ENDORESEMENT DOC NO",
                                		"ENDORSEMENT HOLDER","SIGN","BOND AMOUNT","MICROFILM YEAR","MICROFILM ROLL","MICROFILM BLIP"));
             //   ) {

                        if(!Lst3.isEmpty()) {
                    num = 1;
                    for (CSV_Type_3 obj: Lst3){
                        csvPrinter3.printRecord(Arrays.asList(num, 
                        obj.getErfNo(),
                        obj.getTownshipName(),
                        obj.getPortionNo(),
                        obj.getEndorsementDocNo(),
                        obj.getEndorsementHolder(),
                        obj.getSign(),
                        obj.getBondAmount(),
                        obj.getMicrofilmYear(),
                        obj.getMicrofilmRoll(),
                        obj.getMicrofilmBlip()));
                        num ++;
                    }
    }
    else 
    {
    	csvPrinter3.printRecord ("No record found");
    }
                    csvPrinter3.flush();
                    csvPrinter3.close();
              //  }
             //   catch (IOException ex){}

                    //Process File 4
        outt = dirPath + "/" + "type4.csv";

                //    try (
                            BufferedWriter writer4 = Files.newBufferedWriter(Paths.get(outt));

                            CSVPrinter  csvPrinter4 = new CSVPrinter(writer4, CSVFormat.DEFAULT
                                    .withHeader("SERIAL", "ERF NO","TOWNSHIP NAME","PORTION NO","PERSON NAME","PERSON ID NO","DOC NO","REGISTRATION DATE",
                                    		"SHARE","PURCHASE DATE","SIGN","PURCHASE PRICE","MICROFILM YEAR","MICROFILM ROLL","MICROFILM BLIP"));
              //      ) {

                            if(!Lst4.isEmpty()) {
                        num = 1;
                        for (CSV_Type_4 obj: Lst4){
                            csvPrinter4.printRecord(Arrays.asList(num, 
                            obj.getErfNo(),
                            obj.getTownshipName(),
                            obj.getPortionNo(),
                            obj.getPersonName(),
                            obj.getPersonIdNo(),
                            
                            obj.getDocNo(),
                            obj.getRegistrationDate(),
                            obj.getShare(),
                            obj.getPurchaseDate(),
                            obj.getSign(),
                            obj.getPurchasePrice(),
                            obj.getMicrofilmYear(),
                            obj.getMicrofilmRoll(),
                            obj.getMicrofilmBlip() ));
                            num ++;
                        }
}
else
{
	csvPrinter4.printRecord ("No record found");
}
                        csvPrinter4.flush();
                        csvPrinter4.close();
                //    }
               //     catch (IOException ex){}

                        //Process File 5
        outt = dirPath + "/" + "type5.csv";

                 //       try (
                                BufferedWriter writer5 = Files.newBufferedWriter(Paths.get(outt));

                                CSVPrinter  csvPrinter5 = new CSVPrinter(writer5, CSVFormat.DEFAULT
                                        .withHeader("SERIAL","ERF NO","TOWNSHIP NAME","PORTION NO","PREVIOUS TITLE","REGISTRATION DATE","MICROFILM REFRENCE",
                                        		"SHARE","PURCHASE PRICE","NEW TITLE","MICROFILM REFRENCE1","PREVIOUS OWNER","ID NUMBER"));
              //          ) {

                                if(!Lst5.isEmpty()) {
                            num = 1;
                            for (CSV_Type_5 obj: Lst5){
                                csvPrinter5.printRecord(Arrays.asList(num, 
                                        
                                obj.getErfNo(),
                                obj.getTownshipName(),
                                obj.getPortionNo(),
                                obj.getRegistrationDate(),
                                obj.getMicrofilmRef(),
                                obj.getShare(),
                                obj.getPurchasePrice(),
                                obj.getNewTitle(),
                                obj.getMicrofilmReference(),
                                obj.getPreviousOwner(),
                                obj.getIdNo()));
                                num ++;
                            }
                        }
                        else
                        {
                        	csvPrinter5.printRecord ("No record found");
                        }
                            csvPrinter5.flush();
                            csvPrinter5.close();
               //         }
               //         catch (IOException ex){}

//Process File 6
        outt = dirPath + "/" + "type6.csv";

                   //         try (
                                    BufferedWriter writer6 = Files.newBufferedWriter(Paths.get(outt));

                                    CSVPrinter  csvPrinter6 = new CSVPrinter(writer6, CSVFormat.DEFAULT
                                            .withHeader("SERIAL","ERF NO","TOWNSHIP NAME","PORTION NO","DEEDS OFFICE",
                                            		"PROPERTY TYPE","LPI MINOR KEY","LPI MAJOR KEY"));
               //             ) {

                                    if(!Lst6.isEmpty()) {
                                num = 1;
                                for (CSV_Type_6 obj: Lst6){
                                    csvPrinter6.printRecord(Arrays.asList(num, 
                                     obj.getErfNo(),
                                    obj.getTownshipName(),
                                    obj.getPortionNo(),
                                    obj.getDeedOffice(),
                                    obj.getPropertyType(),
                                    obj.getLPIMinor(),
                                    obj.getLPIMajor()));
                                    num ++;
                                }
                            }
                            else
                            {
                            	csvPrinter6.printRecord ("No record found");
                            }
                                csvPrinter6.flush();
                                csvPrinter6.close();

                        System.out.println("Job Done" );
       // }
      //  catch (IOException ex){}
    }


    public static void DoZipper()
    {
        //AppZip appZip = new AppZip();
        //createFileList(new File(SOURCE_FOLDER));
        createFileList();
        zipIt();
    }


    public static void zipIt( ){
        byte[] buffer = new byte[1024];

        try{
            FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String file : fileList){
                ZipEntry ze= new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in =
                        new FileInputStream(SOURCE_FOLDER + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();

            System.out.println("Zipping Done");
            //Deleting Crumbs
            File folder = new File(SOURCE_FOLDER);
           /// File folder = new File(dir);
            File fList[] = folder.listFiles();

            for (File f : fList) {
                if (f.getName().endsWith(".csv")) {
                    f.delete(); 
                }}
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void createFileList(){

        File dir = new File(SOURCE_FOLDER);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.isFile()){
                    fileList.add(generateZipEntry(child.getAbsoluteFile().toString()));
                }
            }
        }

        else {

        }

    }

   

    private static String generateZipEntry(String file){
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }



}


