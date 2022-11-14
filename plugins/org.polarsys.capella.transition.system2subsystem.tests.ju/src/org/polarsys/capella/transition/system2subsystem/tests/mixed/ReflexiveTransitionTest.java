/*******************************************************************************
 * Copyright (c) 2022 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.transition.system2subsystem.tests.mixed;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.core.data.ctx.CtxPackage;
import org.polarsys.capella.core.data.la.LaPackage;
import org.polarsys.capella.core.model.handler.command.CapellaResourceHelper;
import org.polarsys.capella.transition.system2subsystem.tests.System2SubsystemTest;
import org.polarsys.capella.transition.system2subsystem.tests.TraceabilityReflexiveSID;

/**
 * Reflexive transition (source and target models are the same)
 */
public class ReflexiveTransitionTest extends System2SubsystemTest {

  // Should be transitioned

  public static String PC1 = "506c375f-4df6-4887-82e6-1d6b858d1c55"; //$NON-NLS-1$
  public static String PC2 = "946ca19d-ccee-4eb9-8dd2-c319ff0e826b"; //$NON-NLS-1$
  public static String PC3 = "c27ac2e1-3e6e-4655-b227-48e57d5525fe"; //$NON-NLS-1$
  public static String PC4 = "a39b3c3b-2efc-40cf-992f-3b4351c34a83"; //$NON-NLS-1$
  public static String PF1 = "ed3a7068-723f-4123-9966-0829de231be7"; //$NON-NLS-1$
  public static String PF2 = "d2a5ea4f-c6ab-4aaa-8aac-9dfcd2e3d302"; //$NON-NLS-1$
  public static String PF3 = "f618cdbc-a423-4060-bef7-98c1facea35d"; //$NON-NLS-1$

  @Override
  protected Collection<?> getProjectionElements() {
    return getObjects(PC1);
  }
  
  @Override
  public void setName(String name) {
    super.setName(this.getClass().getName());
  }

  @Override
  public List<String> getRequiredTestModels() {
    return Arrays.asList("Project_test_Reflexive"); //$NON-NLS-1$
  }

  protected String getOutputModelPlatformURIString() {
    return "/Project_test_Reflexive/Project_test_Reflexive."+CapellaResourceHelper.CAPELLA_MODEL_FILE_EXTENSION;
  }

  /**
   * Creates an empty TraceabilitySID, subclasses may override.
   */
  @Override
  protected TraceabilityReflexiveSID createTraceability() {
    return new TraceabilityReflexiveSID();
  }
  

  public static class Interphases extends ReflexiveTransitionTest {

    public Interphases() {
      setKind(Kind.INTER_PHASES);
    }

    @Override
    protected void verify() {
      
      shouldNotBeTransitioned(PC1);
      shouldNotBeTransitioned(PC2);
      shouldNotBeTransitioned(PC3);
      shouldNotBeTransitioned(PC4);
      shouldNotBeTransitioned(PF1);
      shouldNotBeTransitioned(PF2);
      shouldNotBeTransitioned(PF3);
      
      assertTrue(hasSameIDandSID(PC1));
      assertTrue(hasSameIDandSID(PC2));
      assertTrue(hasSameIDandSID(PC3));
      assertTrue(hasSameIDandSID(PC4));
      assertTrue(hasSameIDandSID(PF1));
      assertTrue(hasSameIDandSID(PF2));
      assertTrue(hasSameIDandSID(PF3));
    }

    private boolean hasSameIDandSID(String id) {
      CapellaElement element = getObject(id);
      return element.getId().equals(element.getSid());
    }
  }

  public static class Multiphase extends ReflexiveTransitionTest {

    public Multiphase() {
      setKind(Kind.MULTI_PHASES);
    }

    @Override
    protected void verify() {
      mustBeTransitioned(PC1);
      mustBeTransitioned(PC2);
      mustBeTransitioned(PC3);
      mustBeTransitioned(PF1);
      mustBeTransitioned(PF2);
      mustBeTransitioned(PF3);
      
      CapellaElement sc1 = (CapellaElement) mustBeTransitionedInto(PC1, ComponentType.SYSTEM_STRUCTURE);
      CapellaElement sc2 = (CapellaElement) mustBeTransitionedInto(PC2, ComponentType.SYSTEM_STRUCTURE);
      CapellaElement sf1 = (CapellaElement) mustBeTransitionedInto(PF1, ComponentType.SYSTEM_ANALYSIS);
      CapellaElement sf2 = (CapellaElement) mustBeTransitionedInto(PF2, ComponentType.SYSTEM_ANALYSIS);
      CapellaElement sf3 = (CapellaElement) mustBeTransitionedInto(PF3, ComponentType.SYSTEM_ANALYSIS);
      testReferenceLinked(sc1, sf1, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
      testReferenceLinked(sc1, sf2, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
      testReferenceLinked(sc2, sf3, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
      
      mustBeTransitionedInto(PC1, ComponentType.LOGICAL_STRUCTURE);
      CapellaElement lc2 = (CapellaElement) mustBeTransitionedInto(PC2, ComponentType.LOGICAL_STRUCTURE);
      CapellaElement lc3 = (CapellaElement) mustBeTransitionedInto(PC3, ComponentType.LOGICAL_STRUCTURE);
      CapellaElement lf1 = (CapellaElement) mustBeTransitionedInto(PF1, ComponentType.LOGICAL_ARCHITECTURE);
      CapellaElement lf2 = (CapellaElement) mustBeTransitionedInto(PF2, ComponentType.LOGICAL_ARCHITECTURE);
      CapellaElement lf3 = (CapellaElement) mustBeTransitionedInto(PF3, ComponentType.LOGICAL_ARCHITECTURE);
      testReferenceLinked(lc3, lf1, LaPackage.Literals.LOGICAL_COMPONENT__ALLOCATED_LOGICAL_FUNCTIONS);
      testReferenceLinked(lc3, lf2, LaPackage.Literals.LOGICAL_COMPONENT__ALLOCATED_LOGICAL_FUNCTIONS);
      testReferenceLinked(lc2, lf3, LaPackage.Literals.LOGICAL_COMPONENT__ALLOCATED_LOGICAL_FUNCTIONS);
    }

  }

  public static class Crossphases extends ReflexiveTransitionTest {

    public Crossphases() {
      setKind(Kind.CROSS_PHASES);
    }

    @Override
    protected void verify() {
      mustBeTransitioned(PC1);
      mustBeTransitioned(PC2);
      mustBeTransitionedInto(PC1, ComponentType.SYSTEM_STRUCTURE);
      mustBeTransitionedInto(PC2, ComponentType.SYSTEM_STRUCTURE);
      mustBeTransitioned(PF1);
      mustBeTransitioned(PF2);
      mustBeTransitioned(PF3);
      mustBeTransitionedAndLinkedTo(PC1, PF1, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
      mustBeTransitionedAndLinkedTo(PC1, PF2, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
      mustBeTransitionedAndLinkedTo(PC2, PF3, CtxPackage.Literals.SYSTEM_COMPONENT__ALLOCATED_SYSTEM_FUNCTIONS);
    }
  }
}
