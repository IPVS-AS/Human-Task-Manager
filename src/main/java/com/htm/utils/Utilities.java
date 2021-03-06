/*
 * Copyright 2012 Bangkok Project Team, GRIDSOLUT GmbH + Co.KG, and
 * University of Stuttgart (Institute of Architecture of Application Systems)
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.htm.utils;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.htm.entities.WrappableEntity;
import com.htm.entities.jpa.*;
import com.htm.taskinstance.*;
import com.htm.taskinstance.jpa.*;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import com.htm.exceptions.HumanTaskManagerException;
import com.htm.exceptions.UserException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utilities {

    public static String DATE_TIME_PATTERN = "MM/dd/yyyy KK:mm:ss:S a";

    public static Logger log;

    static {
        log = Utilities.getLogger(Utilities.class);
    }

    // TODO rewrite Logging
    public static Logger getLogger(Class<?> clazz) {
        // DOMConfigurator.configure("log4j/log4j.xml");
        return Logger.getLogger(clazz);
    }

    public static String getStringFromBLOB(byte[] byteArray) {
        if (byteArray != null) {
            return new String(byteArray);

        }
        return null;
    }

    public static byte[] getBLOBFromString(String string) {
        if (string != null) {
            return string.getBytes();
        }
        return null;
    }

    public static int transfrom2PrimaryKey(String pkAsString) {
        if (pkAsString == null) {
            return -1;
        }

        return Integer.valueOf(pkAsString);
    }

    public static String getStringFromXMLDoc(Document doc) {
        XMLOutputter out = new XMLOutputter();
        return out.outputString(doc);
    }

    public static org.w3c.dom.Document getDOMfromString(String xml) throws HumanTaskManagerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource source = new InputSource(new StringReader(xml));
        org.w3c.dom.Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(source);
        } catch (Exception e) {
            throw new HumanTaskManagerException(e.getMessage(), e);
        }
        return document;
    }

    public static Document getXMLFromString(String xml) {

        if (xml == null) {
            return null;
        }

        SAXBuilder builder = new SAXBuilder();
        try {
            return builder.build(new StringReader(xml));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Object getObjectFromBlob(byte[] byteArray) throws HumanTaskManagerException {

        if (byteArray == null) {
            return null;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            return in.readObject();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new HumanTaskManagerException(e);
        }

    }

    /**
     * Serializes an object to a byte array.</br>
     *
     * @param obj The object that has to be serialized. The object must implement
     *            the interface {@link Serializable}
     * @return The serialized object as byte array. <code>null</code> is returned
     *         if the parameter obj is <code>null</code>.
     * @throws HumanTaskManagerException
     */
    public static byte[] getBlobFromObject(Object obj) throws HumanTaskManagerException {

        if (obj == null) {
            return null;
        }

        /* The object must be implement java.io.Serializable */
        if (obj instanceof Serializable) {

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(obj);
                out.close();

                /* Get the bytes of the serialized object */
                return bos.toByteArray();

            } catch (Exception e) {
                log.error(e.getMessage());
                throw new HumanTaskManagerException(e);
            }
        } else {
            String errorMsg = "The object that has to be transformed to a byte array must be serializable, " +
                    "i.e. it must implement java.io.Serializable";
            log.error(errorMsg);
            throw new UserException(errorMsg);
        }

    }


    //TODO method for future checked class cast exceptions
    public static void isValidClass(Object object, Class<?> expectedClass) {

        if (object.getClass() != expectedClass) {
            throw new ClassCastException("Invalid class error. " +
                    "Expected object must be of type " + expectedClass +
                    " but was " + object.getClass());
        }
    }

    public static boolean validateXMLAgainstSchema(Document xml2validate, Document schema) {
        //TODO Implement me
        return true;
    }

    public static boolean shortToBoolean(short number) {
        return number == 1 ? true : false;
    }

    public static short booleanToShort(boolean value) {
        return value ? (short) 1 : (short) 0;
    }

    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp != null) {

            DateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);
            return formatter.format(timestamp.getTime());
        }
        return null;

    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    public static String concateArrayElementsToString(Object[] array) {
        if (array == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            sb.append(array[i] + ", ");
        }
        /* Add the last element to the array without being followed by a comma */
        if (length > 0) {
            sb.append(array[length - 1]);
        }
        return sb.toString();
    }

    public static boolean hasTimeExpired(Timestamp referenceTime) {
        long currentTime = getCurrentTime().getTime();
        /* Only suspend if the point in time not already has passed by */
        if (referenceTime != null &&
                currentTime < referenceTime.getTime()) {
            return false;
        }
        return true;

    }


    public static IAssignedUser createAssignedUserFromEntity(WrappableEntity assignedUserObject) {
        if (assignedUserObject == null) {
            return null;
        }
        /*
           * Check if it is a JPA object that represents the assigned user entity
           */
        Utilities.isValidClass(assignedUserObject, Assigneduser.class);
        return new AssignedUserWrapper((Assigneduser) assignedUserObject);
    }

    public static ITaskInstance createTaskInstanceFromEntity(WrappableEntity taskInstanceObject) {

        Humantaskinstance taskInstanceEntity = null;

        if (taskInstanceObject instanceof Humantaskinstance) {
            taskInstanceEntity = (Humantaskinstance) taskInstanceObject;
            return new TaskInstanceWrapper(taskInstanceEntity);

        } else {
            throw new RuntimeException("Invalid class error. "
                    + "Task instance entity object must be of type "
                    + Humantaskinstance.class);
        }
    }

    public static IAttachment createAttachmentFromEntity(WrappableEntity attachmentObject) {
        Attachment attachmentEntity = null;

        if (attachmentObject instanceof Attachment) {
            attachmentEntity = (Attachment) attachmentObject;
            return new AttachmentWrapper(attachmentEntity);

        } else {
            throw new RuntimeException("Invalid class error. "
                    + "Attachment entity object must be of type "
                    + Attachment.class);
        }
    }

    public static ICorrelationProperty createCorrelationPropertyFromEntity(WrappableEntity correlationPropsObject) {
        Callbackcorrelationproperty attachmentEntity = null;

        if (correlationPropsObject instanceof Callbackcorrelationproperty) {
            attachmentEntity = (Callbackcorrelationproperty) correlationPropsObject;
            return new CorrelationPropertiesWrapper(attachmentEntity);

        } else {
            throw new RuntimeException("Invalid class error. "
                    + "Correlation property entity object must be of type "
                    + Attachment.class);
        }
    }

    public static IFault createFault(String name, Object faultData) {
        return new FaultImpl(name, faultData);
    }

    public static IWorkItem createWorkItemFromEntity(Object workItemObject) {
        Utilities.isValidClass(workItemObject, Workitem.class);

        return new WorkItemWrapper((Workitem) workItemObject);
    }
}
