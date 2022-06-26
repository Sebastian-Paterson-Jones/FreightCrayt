package com.example.freightcrayt.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DataHelper {

    public static String getUsername() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getDisplayName();
    }

    public static String getUserEmail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getEmail();
    }

    public static String getUserID() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getUid();
    }

    public static boolean addUserCategory(String title, int goal, String description) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String currentUserID = getUserID();
        final boolean[] res = {true};

        // save category to categories collection
        DatabaseReference categoryRef = db.getReference("Categories").push();
        Collection newCollection = new Collection(title, goal, description, categoryRef.getKey());
        categoryRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        if(!res[0]) {
            return res[0];
        }

        // save category to categories collection
        DatabaseReference userCollectionsRef = db.getReference("UserCategories/" + getUserID()).push();
        userCollectionsRef.setValue(categoryRef.getKey(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public static boolean removeCategory(String collectionID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference categoryRef = db.getReference("Categories");
        categoryRef.child(collectionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                res[0] = false;
            }
        });

        return res[0];
    }

    public static boolean addCategoryItem(String title, String acquisitionDate, String description, String collectionID, @Nullable Bitmap image, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        // save item to collection
        DatabaseReference itemsRef = db.getReference("Items").push();

        if(image != null) {
            putFirebaseImage(image, itemsRef.getKey()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    CollectionItem newCollection = new CollectionItem(title, acquisitionDate, description, collectionID, task.getResult().toString(), itemsRef.getKey());
                                    itemsRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if(error != null) {
                                                res[0] = false;
                                            } else {
                                                incrementCollectionSize(collectionID, collectionSize);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        res[0] = false;
                    }
                }
            });
        } else {
            CollectionItem newCollection = new CollectionItem(title, acquisitionDate, description, collectionID, itemsRef.getKey());
            itemsRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error != null) {
                        res[0] = false;
                    } else {
                        incrementCollectionSize(collectionID, collectionSize);
                    }
                }
            });
        }

        return res[0];
    }

    public static boolean editCategoryItem(String title, String acquisitionDate, String description, String collectionID, @Nullable Bitmap image, String imageUrl, String itemID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        // save item to collection
        DatabaseReference itemsRef = db.getReference("Items").child(itemID);

        if(image != null) {
            putFirebaseImage(image, itemID).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    CollectionItem item = new CollectionItem(title, acquisitionDate, description, collectionID, task.getResult().toString(), itemID);
                                    itemsRef.setValue(item, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if(error != null) {
                                                res[0] = false;
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        res[0] = false;
                    }
                }
            });
        } else {
            CollectionItem newCollection = new CollectionItem(title, acquisitionDate, description, collectionID, imageUrl, itemID);
            itemsRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error != null) {
                        res[0] = false;
                    }
                }
            });
        }

        return res[0];
    }

    public static boolean removeCategoryItem(String collectionID, int collectionSize, String itemID, String imageURL) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        removeFirebaseImage(imageURL, itemID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    DatabaseReference itemsRef = db.getReference("Items");
                    itemsRef.child(itemID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                decrementCollectionSize(collectionID, collectionSize);
                            } else {
                                res[0] = false;
                            }
                        }
                    });
                } else {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public static UploadTask putFirebaseImage(Bitmap image, String itemID) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference imageRef = imagesRef.child(itemID);
        return imageRef.putBytes(getBytesFromBitmap(image));
    }

    public static Task<Void> removeFirebaseImage(String imageUrl, String itemID) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference imageRef = imagesRef.child(itemID);
        return imageRef.delete();
    }

    public static boolean addUserToCategory(String collectionID, String userID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference userRef = db.getReference("CollectionCollaborations").child(collectionID).child(userID);
        userRef.setValue(true, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public static boolean removeUserFromCategory(String collectionID, String userID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference userRef = db.getReference("CollectionCollaborations").child(collectionID);
        userRef.child(userID).removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res[0] = false;
            }
        });

        return res[0];
    }

    public static void incrementCollectionSize(String collectionID, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Categories").child(collectionID).child("size").setValue(collectionSize + 1);
    }

    public static void decrementCollectionSize(String collectionID, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Categories").child(collectionID).child("size").setValue(collectionSize - 1);
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
