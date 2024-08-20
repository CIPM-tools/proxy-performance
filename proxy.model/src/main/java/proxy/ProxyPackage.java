/**
 */
package proxy;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see proxy.ProxyFactory
 * @model kind="package"
 * @generated
 */
public interface ProxyPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "proxy";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "model://proxy.ecore/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "proxy";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProxyPackage eINSTANCE = proxy.impl.ProxyPackageImpl.init();

	/**
	 * The meta object id for the '{@link proxy.impl.AImpl <em>A</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see proxy.impl.AImpl
	 * @see proxy.impl.ProxyPackageImpl#getA()
	 * @generated
	 */
	int A = 0;

	/**
	 * The feature id for the '<em><b>No Proxy No Con</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__NO_PROXY_NO_CON = 0;

	/**
	 * The feature id for the '<em><b>No Proxy Con</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__NO_PROXY_CON = 1;

	/**
	 * The feature id for the '<em><b>Proxy No Con</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__PROXY_NO_CON = 2;

	/**
	 * The feature id for the '<em><b>Proxy Con</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__PROXY_CON = 3;

	/**
	 * The number of structural features of the '<em>A</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A_FEATURE_COUNT = 4;


	/**
	 * Returns the meta object for class '{@link proxy.A <em>A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>A</em>'.
	 * @see proxy.A
	 * @generated
	 */
	EClass getA();

	/**
	 * Returns the meta object for the reference '{@link proxy.A#getNoProxyNoCon <em>No Proxy No Con</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>No Proxy No Con</em>'.
	 * @see proxy.A#getNoProxyNoCon()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_NoProxyNoCon();

	/**
	 * Returns the meta object for the containment reference '{@link proxy.A#getNoProxyCon <em>No Proxy Con</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>No Proxy Con</em>'.
	 * @see proxy.A#getNoProxyCon()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_NoProxyCon();

	/**
	 * Returns the meta object for the reference '{@link proxy.A#getProxyNoCon <em>Proxy No Con</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Proxy No Con</em>'.
	 * @see proxy.A#getProxyNoCon()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_ProxyNoCon();

	/**
	 * Returns the meta object for the containment reference '{@link proxy.A#getProxyCon <em>Proxy Con</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Proxy Con</em>'.
	 * @see proxy.A#getProxyCon()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_ProxyCon();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ProxyFactory getProxyFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link proxy.impl.AImpl <em>A</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see proxy.impl.AImpl
		 * @see proxy.impl.ProxyPackageImpl#getA()
		 * @generated
		 */
		EClass A = eINSTANCE.getA();

		/**
		 * The meta object literal for the '<em><b>No Proxy No Con</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__NO_PROXY_NO_CON = eINSTANCE.getA_NoProxyNoCon();

		/**
		 * The meta object literal for the '<em><b>No Proxy Con</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__NO_PROXY_CON = eINSTANCE.getA_NoProxyCon();

		/**
		 * The meta object literal for the '<em><b>Proxy No Con</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__PROXY_NO_CON = eINSTANCE.getA_ProxyNoCon();

		/**
		 * The meta object literal for the '<em><b>Proxy Con</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__PROXY_CON = eINSTANCE.getA_ProxyCon();

	}

} //ProxyPackage
