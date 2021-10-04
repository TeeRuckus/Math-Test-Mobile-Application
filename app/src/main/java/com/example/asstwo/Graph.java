package com.example.asstwo;

import static com.example.assone.myUtils.cleanString;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Graph implements Serializable
{
    public class Vertex implements Serializable
    {
        private String key;
        private User value;
        private HashMap<String, Vertex> connections;


        public Vertex()
        {
            key  = "EMPTY";
            value = null;
            connections = new HashMap<String, Vertex>();
        }

        public Vertex(String inKey, Admin inUser)
        {
            if( validateKey(inKey) && validateUser(inUser))
            {
                key = inKey;
                value = inUser;
                connections = new HashMap<String, Vertex>();
            }
        }

        public Vertex(String inKey, Instructor inUser)
        {
            if( validateKey(inKey) && validateUser(inUser))
            {
                key = inKey;
                value = inUser;
                connections = new HashMap<String, Vertex>();
            }
        }

        public Vertex(String inKey, Student inUser)
        {
            if( validateKey(inKey) && validateUser(inUser))
            {
                key = inKey;
                value = inUser;
                connections = new HashMap<String, Vertex>();
            }
        }

        public Vertex(Vertex inVert)
        {
            key = new String(inVert.key);
            /*
            TODO:
                - I don't know how you're going to have a copy of this maybe what you could have
                is a clone method in user
             */
            value = inVert.value;
            connections = new HashMap<String, Vertex>(inVert.connections);
        }

        //ACCESSORS
        public String getKey()
        {
            return new String(key);
        }

        public User getValue()
        {
            return value;
        }

        public HashMap<String, Vertex> getConnections()
        {
            return connections;
        }


        //MUTATORS
        public void setKey(String inKey)
        {
            inKey = myUtils.cleanString(inKey);
            if(validateKey(inKey))
            {
                key = inKey;
            }

        }



        public void setValue(Admin inUser)
        {
            value = inUser;
        }

        public void setValue(Instructor inUser)
        {
            value = inUser;
        }

        public void setValue(Student inUser)
        {
            value = inUser;
        }

        public int size()
        {
            return connections.size();
        }

        public String getType()
        {
            return value.getType();
        }

        private boolean validateKey(String inKey)
        {
            boolean valid = true;
            if(inKey.length() == 0)
            {
                throw new IllegalArgumentException("ERROR: can't have an empty string as a key: " + inKey);
            }
            return valid;
        }

        private boolean validateUser(Admin inUser)
        {
            boolean valid = true;
            if( !(inUser instanceof Admin))
            {
                throw new IllegalArgumentException("ERROR: object must doesn't meet required type: " + inUser);
            }
            return valid;
        }

        private boolean validateUser(Instructor inUser)
        {
            boolean valid = true;
            if( !(inUser instanceof Instructor))
            {
                throw new IllegalArgumentException("ERROR: object must doesn't meet required type: " + inUser);
            }
            return valid;
        }

        private boolean validateUser(Student inUser)
        {
            boolean valid = true;
            if( !(inUser instanceof Student))
            {
                throw new IllegalArgumentException("ERROR: object must doesn't meet required type: " + inUser);
            }
            return valid;
        }

    }

    private static final String TAG = "Graph.";
    private HashMap<String, Vertex> vertices;
    private String currentAdmin = "ADMIN";

    //DEFAULT CONSTRUCTOR
    public Graph()
    {
        vertices = new HashMap<String, Vertex>();
        vertices.put(currentAdmin, null);
    }

    public int size()
    {
        return vertices.size();
    }

    public String getAdmin()
    {
        return new String(currentAdmin);
    }

    public void setAdmin(String inAdmin)
    {
        if(validateName(inAdmin))
        {

            //once you update the admin, you will have to change the current node
            Vertex currAdmin = getVertex();
            currAdmin.setKey(inAdmin);
            Vertex copyAdmin = new Vertex(currAdmin);

            //removing the old admin
            vertices.remove(currentAdmin);
            inAdmin = myUtils.cleanString(inAdmin);

            //put the new admin node into the graph with the new name
            currentAdmin = inAdmin;

            vertices.put(inAdmin, copyAdmin);

        }
    }

    //TODO: you will need to add more addVertex method for the other of vertex types which you can use in the programme
    public void addVertex(Instructor inInstrucor)
    {
        //can't add a vertex if they is not root node in the programme
        if(validateRootNode())
        {
            if (doesNotExist(inInstrucor.getName())) {
                String key = myUtils.cleanString(inInstrucor.getName());
                Vertex newVert = new Vertex(key, inInstrucor);
                vertices.put(key, newVert);

                //the instructor node is always going to be connected to the admin node
                Vertex adminNode = vertices.get(currentAdmin);
                adminNode.connections.put(myUtils.cleanString(inInstrucor.getName()), newVert);
            }
        }
    }

    public void addVertex(Student inStudent)
    {
        //can't add a vertex if they is no root node
        if(validateRootNode())
        {
            if(doesNotExist(inStudent.getName())) {
                String key = myUtils.cleanString(inStudent.getName());
                Vertex newVert = new Vertex(key, inStudent);
                vertices.put(key, newVert);

                //this add method is going to automatically going to attach the student to the admin
                Vertex adminNode = vertices.get(currentAdmin);
                adminNode.connections.put(myUtils.cleanString(inStudent.getName()), newVert);
            }
        }
    }

    //if we know the instructor we can connect the student to the instructor instead of making the
    //default connection
    public void addVertex(Student inStudent, String inInstructor)
    {
        //finding the vertex in the current vertices
        inInstructor = myUtils.cleanString(inInstructor);
        Vertex currInstructor = vertices.get(inInstructor);

        if (validateInstructor(currInstructor))
        {
            String studentName = myUtils.cleanString(inStudent.getName());
            Vertex currStudent = new Vertex(studentName, inStudent);
            currInstructor.connections.put(studentName, currStudent);
            //putting the student vertex on the main graph data structure
            vertices.put(studentName, currStudent);
        }
    }

    public void addVertex(Admin inAdmin)
    {
        //they is going ot be null at the current location
        if(rootExists())
        {
            Vertex adminVert = new Vertex(currentAdmin, inAdmin);
            vertices.replace(currentAdmin, adminVert);
        }
    }

    //no arguments is going to assumme to get what is at the admin vertex
    public Vertex getVertex()
    {
        return vertices.get(currentAdmin);
    }

    public Vertex getVertex(String inVertex)
    {
        //hashmap will throw an error if it doesn't exist in current table
        inVertex = myUtils.cleanString(inVertex);
        Vertex retVert = vertices.get(inVertex);
        validateRetrival(retVert, inVertex);

        return retVert;
    }

    public Vertex delVertex(String key)
    {
        Vertex delVert = null;
        key = myUtils.cleanString(key);
        //they is always going to be an admin key which exists, so when the vertices is empty
        //it actually only has one node, which is the admin. Which either is null or has something
        //in it, and since you can't delete the admin node this class should break
        if(vertices.size() == 1)
        {
            throw  new IllegalArgumentException("ERROR: no vertices have being added as yet: " + vertices.size());
        }

        //if the code has made it here, means that they is more than one node in the network
        Vertex currVert = vertices.get(key);
        validateRetrival(currVert, key);

        if (currVert.connections.isEmpty())
        {
            //students are not going to be connected to anything therefore need to check if it's a student
            if (currVert.getType().equals("STUDENT"))
            {
            /*
            for a student the student must be deleted in two places, the first place being inside
            the actual graph itself and the second place being in th connections of the instructor
            node which it was connected too previously
             */

                User currUser = currVert.value;
                Student currStudent  = (Student) currUser;
                validateRetrival(currStudent, "student");
                String currInstructor = currStudent.getInstructor();

                //getting the instructor node from the main graph
                currInstructor = myUtils.cleanString(currInstructor);
                Vertex instructorNode = vertices.get(currInstructor);

                //deleting the student from the instructor list
                String studentName = myUtils.cleanString(currStudent.getName());
                //TODO: what you can do is that you can add an if statement to protect
                instructorNode.connections.remove(studentName);
            }

            //need to check if the current node is going to be an instructor node for deletion from the admin vertices
            if(currVert.getType().equals("INSTRUCTOR"))
            {
                Vertex adminNode = vertices.get(currentAdmin);
                adminNode.connections.remove(key);
            }

            //if the vertex is not connected to anything else, just remove the vertex
            delVert = vertices.remove(key);
        }
        else if (currVert.getType().equals("INSTRUCTOR"))
        {
            //get everything which the instructor is connected too which is going to be all the students
            Set<String> keys = currVert.connections.keySet();

            //grabbing the admin node
            Vertex adminNode = vertices.get(currentAdmin);

            //going through everything which the to be deleted node was attached too, and attaching to admin node
            for (String currKey : keys)
            {
                Vertex copyVert = currVert.connections.get(currKey);
                // all the vertices whcih the instructor was connected too was going to be a student node
                // hence, changing the current owner from instructor to the admin
                Student currStudent = (Student) copyVert.getValue();
                currStudent.setInstructor(currentAdmin);

                //making a copy so that the deletion process won't conflict with the nodes in admin
                adminNode.connections.put(currKey, new Vertex(copyVert));
            }

            //once the copy process has completed we can now actually delete the node of interest
            delVert = vertices.remove(key);
        }
        return delVert;
    }

    public HashMap<String, Vertex> getVertices()
    {
        return new HashMap<>(vertices);
    }

    public void update(String oldNode, Vertex newNode)
    {
        oldNode = myUtils.cleanString(oldNode);
        vertices.remove(oldNode);
        vertices.put(newNode.getKey(), newNode);
    }



    //loading the different vertices depending on the user which is going to be using the graph structure
    public ArrayList<Vertex> adminStudentLoad()
    {
        ArrayList<Vertex> retList = new ArrayList<>();
        Set<String> keys = vertices.keySet();
        //creating an arraylist as it's a mutable data structure and can actually sort over
        List<String> keysOrdered = new ArrayList<>();

        // transferring keys to keysOrdered
        for (String currKey : keys)
        {
            keysOrdered.add(currKey);
        }

        //we want to sort the keys before we retrieve them from the graph
        Collections.sort(keysOrdered);

        for(String currKey : keysOrdered)
        {
            if (vertices.get(currKey).getValue().getType().equals("STUDENT"))
            {
                //grabbing the vertices in sorted order
                retList.add(vertices.get(currKey));
            }
        }


        //this method is going to be used for viewing purposes hence we want to return a copy of the hash map
        return retList;
    }

    public ArrayList<Vertex> adminInstructorLoad()
    {
        ArrayList<Vertex> retList = new ArrayList<>();
        Set<String> keys = vertices.keySet();
        //creating an arrayLIst as it's mutable data structure and can actaully sort over them
        List<String> keysOrdered = new ArrayList<>();

        // ttranferrin keys to keysOrdered
        for (String currKey : keys)
        {
            keysOrdered.add(currKey);
        }

        //actually sorting the keys so that they will appear in alphabetical order
        Collections.sort(keysOrdered);

        for(String currKey : keysOrdered)
        {
            if(vertices.get(currKey).getValue().getType().equals("INSTRUCTOR"))
            {
                //grabbing the isntructor vertices in sorted order
                retList.add(vertices.get(currKey));
            }
        }

        return retList;
    }

    public ArrayList<Vertex> instructorLoad(String inInstructor)
    {
        ArrayList<Vertex> retList = new ArrayList<>();
        //this method is going to be used for viewing purposes hence we want to return a copy of the hash map
        //the instructor can only view the students which are connected to him
        Vertex currInstructor = getVertex(inInstructor);

        //double checking if the retrieved vertex is actually going to be an instructor
        if(!(currInstructor.value.getType().equals("INSTRUCTOR")))
        {
            throw new IllegalArgumentException("ERROR: can only load instructor vertexs");
        }

        Set<String> keys = currInstructor.connections.keySet();
        //creating an arrayList as it's a mutable data structure and we can sort over teh data structure
        List<String> keysOrdered = new ArrayList<>();

        //transferring keys to keysOrdered
        for (String currKey : keys)
        {
            Log.e(TAG, "Current Key: " + currKey);
            keysOrdered.add(currKey);
        }

        //we want to sort the keys before we retrieve tehm from teh graph
        Collections.sort(keysOrdered);

        for(String currKey  : keysOrdered)
        {
            //grabbing the vertices in sorted order
            retList.add(currInstructor.connections.get(currKey));
        }

        //once we're confident that an instructor has being loaded we can get all the instructors students and return them as a copy as they
        //should be only for viewing purposes and nothing more
        //return new HashMap<>(currInstructor.connections);
        return retList;
    }

    // adding the current practical to the students whcih are on the current network
    public void sendPracticals(TestHistory inPrac)
    {
        // adding the practicals to the admin, so if a new student is added by the admin, the admin can
        // give the practicals which the amdin has

        Vertex adminVert = getVertex();
        adminVert.getValue().addPrac(inPrac);

        Set<String > allVertices = vertices.keySet();

        for (String currVertKey : allVertices)
        {
            Log.e(TAG, "Current vert: " + currVertKey);
            // making sure that a practical copy is going to be returned, so all practical objects
            // don't reference each otehr when changes are being made
            TestHistory tempPrac = new TestHistory(inPrac);

            Vertex  currVert = getVertex(currVertKey);
            User currUser = currVert.getValue();

            //if the current user is going to be a student add teh practical object onto the user
            if (currUser.getType().equals("INSTRUCTOR"))
            {
                currUser.addPrac(tempPrac);
            }

        }
    }

    public boolean isEmpty()
    {
        boolean valid = false;

        //if they is more than the admin node in the graph then the graph structure is not empty
        if(vertices.size() <= 1)
        {
            valid = true;
        }
        return valid;
    }

    public void moveStudent(String studentName, String newInsructor)
    {
        //TODO: I don't think you will need this here, but if you need it come back and implement this
    }
    //TODO: you can honestly delete all these functions which are going to be below here
    /***********************************************************************************************
     * PUPROSE: to add a directed edge from one node to another node. Otherwise, if this relationship
     * doesn't exist fail
     ***********************************************************************************************/
    public void addEdge(String fromEdge, String toEdge)
    {
        fromEdge = myUtils.cleanString(fromEdge);
        toEdge = myUtils.cleanString(toEdge);
        //if the vertex doesn't exist, these operations will fail
        Vertex fromVertex = vertices.get(fromEdge);
        Vertex toVertex = vertices.get(toEdge);

        //getting all the connections from the fromVertex, and the putting the to vertex in the connections
        fromVertex.connections.put(toEdge, toVertex);
    }

    /***********************************************************************************************
     * PUPROSE: to delete a directed edge from one node to another node. Otherwise, if this
     * relationship doesn't excist fail
     ***********************************************************************************************/
    public void delEdge(String fromEdge, String toEdge)
    {
        fromEdge = myUtils.cleanString(fromEdge);
        toEdge = myUtils.cleanString(toEdge);
        //if the vertex doesn't exist, these operations will fail
        Vertex fromVertex = vertices.get(fromEdge);
        Vertex toVertex = vertices.get(toEdge);
        fromVertex.connections.remove(toEdge);
    }

    public HashMap<String, Vertex> getEdges(String nodeName)
    {
        nodeName = myUtils.cleanString(nodeName);
        Vertex currVertex = vertices.get(nodeName);
        return currVertex.connections;
    }

    private boolean validateInstructor(Vertex inVert)
    {
        boolean valid = true;
        String type = inVert.value.getType();

        if (!(type.equals("INSTRUCTOR")))
        {
            throw new IllegalArgumentException("ERROR: students can only be added to an instructor");
        }
        return valid;
    }
    private boolean validateRootNode()
    {
        boolean valid = true;
        //grab what is at the curren root place
        Vertex rootNode = vertices.get(currentAdmin);
        if(rootNode == null)
        {
            throw new IllegalArgumentException("ERROR: they is admin, must create admin first");
        }
        return valid;
    }

    private boolean rootExists()
    {
        boolean valid = true;
        //if they is something here, it means that an admin node has already being created
        if(vertices.get(currentAdmin) != null)
        {
            throw new IllegalArgumentException("ERORR: an admin already exist for this application");
        }

        return valid;
    }

    private boolean validateRetrival(Object inObj, String key)
    {
        boolean valid = true;
        if (inObj == null)
        {
            throw new IllegalArgumentException("Error: vertex " + key  + " does not exist in graph");
        }
        return valid;
    }

    private boolean validateName(String inName)
    {
        boolean valid = true;
        if (inName.length() == 0)
        {
            throw new IllegalArgumentException("ERROR: please enter a valid name");
        }

        return valid;
    }

    private boolean doesNotExist(String inUser)
    {
        boolean valid = true;
        inUser = myUtils.cleanString(inUser);

        // if they is someone they, the programme should complain
        if(vertices.get(inUser) != null)
        {
            throw new IllegalArgumentException("ERROR: user " + inUser + " already exists in graph");
        }

        return valid;
    }

    public boolean UserDoesNotExist(String inUser)
    {
        boolean valid = true;
        inUser = myUtils.cleanString(inUser);

        // if they is someone they, the programme should complain
        if(vertices.get(inUser) != null)
        {
            valid = false;
        }

        return valid;
    }

    public String toString()
    {
        throw new IllegalArgumentException("ERROR: to be implemented");
    }
}
