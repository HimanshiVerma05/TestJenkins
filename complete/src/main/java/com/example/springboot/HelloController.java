package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.azure.storage.*;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {

	List<String> files = new ArrayList<String>();
	   private final Path rootLocation = Paths.get("_Path_To_Save_The_File");
	
	// Define the connection-string with your values
	   public static final String storageConnectionString =
	       "DefaultEndpointsProtocol=http;" +
	       "AccountName=your_storage_account;" +
	       "AccountKey=your_storage_account_key";
	   
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@CrossOrigin(origins = "http://localhost:4200")	
	@PostMapping("/savefile")
	   public String handleFileUpload(@RequestParam("file") MultipartFile file) {
	      String message;
//	      try {
//	         try {
//	            Files.copy(file.getInputStream(), this.rootLocation.resolve("file_name.pdf"));
//	         } catch (Exception e) {
//	            throw new RuntimeException("FAIL!");
//	         }
//	         files.add(file.getOriginalFilename());
//
//	         message = "Successfully uploaded!";
//	         return ResponseEntity.status(HttpStatus.OK).body(message);
//	      } catch (Exception e) {
//	         message = "Failed to upload!";
//	         return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//	      }
	      System.out.println("Inside save file method. ");
	      
	      BlobContainerClient containerClient=null;
	      String yourSasToken = "<insert-your-sas-token>";
	      /* Create a new BlobServiceClient with a SAS Token */
	      BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
	          .endpoint("https://your-storage-account-url.storage.windows.net")
	          .sasToken(yourSasToken)
	          .buildClient();

	      /* Create a new container client */
	      try {
	          containerClient = blobServiceClient.createBlobContainer("my-container-name");
	      } catch (BlobStorageException ex) {
	          // The container may already exist, so don't throw an error
	          if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
	              throw ex;
	          }
	      }

	      /* Upload the file to the container */
	      BlobClient blobClient = containerClient.getBlobClient("my-remote-file.jpg");
	      blobClient.uploadFromFile("my-local-file.jpg");
	      
	      return "success";
	   }


}
