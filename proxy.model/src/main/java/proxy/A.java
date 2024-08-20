/**
 */
package proxy;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link proxy.A#getNoProxyNoCon <em>No Proxy No Con</em>}</li>
 *   <li>{@link proxy.A#getNoProxyCon <em>No Proxy Con</em>}</li>
 *   <li>{@link proxy.A#getProxyNoCon <em>Proxy No Con</em>}</li>
 *   <li>{@link proxy.A#getProxyCon <em>Proxy Con</em>}</li>
 * </ul>
 *
 * @see proxy.ProxyPackage#getA()
 * @model
 * @generated
 */
public interface A extends EObject {
	/**
	 * Returns the value of the '<em><b>No Proxy No Con</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>No Proxy No Con</em>' reference.
	 * @see #setNoProxyNoCon(A)
	 * @see proxy.ProxyPackage#getA_NoProxyNoCon()
	 * @model resolveProxies="false"
	 * @generated
	 */
	A getNoProxyNoCon();

	/**
	 * Sets the value of the '{@link proxy.A#getNoProxyNoCon <em>No Proxy No Con</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>No Proxy No Con</em>' reference.
	 * @see #getNoProxyNoCon()
	 * @generated
	 */
	void setNoProxyNoCon(A value);

	/**
	 * Returns the value of the '<em><b>No Proxy Con</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>No Proxy Con</em>' containment reference.
	 * @see #setNoProxyCon(A)
	 * @see proxy.ProxyPackage#getA_NoProxyCon()
	 * @model containment="true"
	 * @generated
	 */
	A getNoProxyCon();

	/**
	 * Sets the value of the '{@link proxy.A#getNoProxyCon <em>No Proxy Con</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>No Proxy Con</em>' containment reference.
	 * @see #getNoProxyCon()
	 * @generated
	 */
	void setNoProxyCon(A value);

	/**
	 * Returns the value of the '<em><b>Proxy No Con</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proxy No Con</em>' reference.
	 * @see #setProxyNoCon(A)
	 * @see proxy.ProxyPackage#getA_ProxyNoCon()
	 * @model
	 * @generated
	 */
	A getProxyNoCon();

	/**
	 * Sets the value of the '{@link proxy.A#getProxyNoCon <em>Proxy No Con</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Proxy No Con</em>' reference.
	 * @see #getProxyNoCon()
	 * @generated
	 */
	void setProxyNoCon(A value);

	/**
	 * Returns the value of the '<em><b>Proxy Con</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proxy Con</em>' containment reference.
	 * @see #setProxyCon(A)
	 * @see proxy.ProxyPackage#getA_ProxyCon()
	 * @model containment="true"
	 * @generated
	 */
	A getProxyCon();

	/**
	 * Sets the value of the '{@link proxy.A#getProxyCon <em>Proxy Con</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Proxy Con</em>' containment reference.
	 * @see #getProxyCon()
	 * @generated
	 */
	void setProxyCon(A value);

} // A
