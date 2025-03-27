package backend.controller;

import backend.exception.InventoryNotFoundException;
import backend.model.InventoryModel;
import backend.repository.InventoryRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

//     @GetMapping("/")
//     public String rootMap() {
//         String message = "Hello World";
//         return message;
//     }

//     @GetMapping("/hello")
//     public String helloEndpoint(@RequestParam(value = "name", defaultValue = "World") String name) {
//         return "Hello, " + name + "!";
//     }

//     @GetMapping("/greet")
//     public String greet() {
//         return "Welcome to Spring Boot!";
//     }


//     @GetMapping("/greet/{name}")
// public String greetWithCustomMessage(
//         @PathVariable String name,
//         @RequestParam(value = "message", required = false) String message) {

//     if (message == null || message.isEmpty()) {
//         message = "Welcome to Spring Boot!";
//     }

//     return "Hello " + name + "! " + message;
// }






    @PostMapping("/Inventory")
    public InventoryModel newInventoryModel(@RequestBody InventoryModel newInventoryModel) {

        return inventoryRepository.save(newInventoryModel);

    }

    @PostMapping("inventory/itemImg")
    public String itemImage(@RequestParam("file") MultipartFile file) {
        String folder = "C:/Users/sumet/Desktop/project/spring-crud/spring-CRUD/pto/";
        
        String itemImage = file.getOriginalFilename();

        try {
            File uplodDir = new File(folder);
            if (!uplodDir.exists()) {
                uplodDir.mkdir();
            }
            file.transferTo(Paths.get(folder + itemImage));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file: " + itemImage;
        }
        return itemImage;
    }

    @GetMapping("/inventory")

    public List<InventoryModel> getAllItems() {
        List<InventoryModel> items = inventoryRepository.findAll();
        return items; // Ensure proper serialization
    }

    @GetMapping("/inventory/{id}")
    InventoryModel getItemId(@PathVariable Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new InventoryNotFoundException(id));
    }

    private final String UPLOAD_DIR = "C:/Users/sumet/Desktop/project/spring-crud/spring-CRUD/pto/";

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String filename) {
        System.out.println("Requesting image: " + filename); // Log the requested filename

        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename); // Log if the file does not exist

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @PutMapping("/inventory/{id}")
    public InventoryModel updateItem(
            @RequestPart(value = "itemDetails") String itemDetails,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable Long id) {
        System.out.println("Item Details: " + itemDetails);
        if (file != null) {
            System.out.println("File received: " + file.getOriginalFilename());
        } else {
            System.out.println("No file uploaded");
        }

        ObjectMapper mapper = new ObjectMapper();
        InventoryModel newInventory;
        try {
            newInventory = mapper.readValue(itemDetails, InventoryModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing itemDetails", e);
        }

        return inventoryRepository.findById(id).map(existingInventory -> {
            existingInventory.setItemId(newInventory.getItemId());
            existingInventory.setItemName(newInventory.getItemName());
            existingInventory.setItemCategory(newInventory.getItemCategory());
            existingInventory.setItemQty(newInventory.getItemQty());
            existingInventory.setItemDetails(newInventory.getItemDetails());

            if (file != null && !file.isEmpty()) {
                String folder = "C:/Users/sumet/Desktop/project/spring-crud/spring-CRUD/pto/";
                String itemImage = file.getOriginalFilename();
                try {
                    file.transferTo(Paths.get(folder + itemImage));
                    existingInventory.setItemImage(itemImage);
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading file", e);
                }
            }
            return inventoryRepository.save(existingInventory);
        }).orElseThrow(() -> new InventoryNotFoundException(id));
    }

}
