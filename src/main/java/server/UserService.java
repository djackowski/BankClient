
package server;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "UserService", targetNamespace = "http://server/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UserService {


    /**
     * 
     * @param arg0
     * @throws Exception_Exception
     */
    @WebMethod
    @Action(input = "http://server/UserService/removeUserRequest", output = "http://server/UserService/removeUserResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/removeUser/Fault/Exception")
    })
    public void removeUser(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @throws Exception_Exception
     */
    @WebMethod
    @Action(input = "http://server/UserService/loginRequest", output = "http://server/UserService/loginResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/login/Fault/Exception")
    })
    public void login(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws Exception_Exception
    ;

    /**
     * 
     * @return
     *     returns server.UserList
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://server/UserService/getAllUsersRequest", output = "http://server/UserService/getAllUsersResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/getAllUsers/Fault/Exception")
    })
    public UserList getAllUsers()
        throws Exception_Exception
    ;

    /**
     * 
     * @throws Exception_Exception
     */
    @WebMethod
    @Action(input = "http://server/UserService/logoutRequest", output = "http://server/UserService/logoutResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/logout/Fault/Exception")
    })
    public void logout()
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @throws Exception_Exception
     */
    @WebMethod
    @Action(input = "http://server/UserService/createUserRequest", output = "http://server/UserService/createUserResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/createUser/Fault/Exception")
    })
    public void createUser(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws Exception_Exception
    ;

    /**
     * 
     * @return
     *     returns server.User
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://server/UserService/getCurrentUserRequest", output = "http://server/UserService/getCurrentUserResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server/UserService/getCurrentUser/Fault/Exception")
    })
    public User getCurrentUser()
        throws Exception_Exception
    ;

}
