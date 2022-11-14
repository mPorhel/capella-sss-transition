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
package org.polarsys.capella.transition.system2subsystem.tests;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.core.transition.common.capellaHelpers.HashMapSet;

/**
 * This Traceability registry shall only be used when transition is performed on same model
 */
public class TraceabilityReflexiveSID extends TraceabilitySID {

  public void init(Resource source, Resource target) {
    Iterator<EObject> it = source.getAllContents();

    sourceMap = new HashMapSet<String, EObject>();
    targetMap = new HashMapSet<String, EObject>();

    while (it.hasNext()) {
      EObject obj = it.next();
      if (obj instanceof ModelElement) {
        String id = getSourceId(obj);
        if (id != null && !id.equals("")) {
          EObject sourceObject = getEObject(source, id);
  
          sourceMap.put(id, sourceObject);
          targetMap.put(id, obj);
        }
      }
    }
  }

  public String getSourceId(EObject element) {
    ModelElement elem = (ModelElement) element;
    String sid = elem.getSid();
    if (elem.getId().equals(sid)) {
      return null;
    }
    return sid;
  }

  /**
   * @param source_p
   * @param id_p
   * @return
   */
  public EObject getEObject(Resource source_p, String id_p) {
    return source_p.getEObject(id_p);
  }

  public Collection<EObject> getTracedObjects(String sourceId_p) {
    return targetMap.get(sourceId_p);
  }

  public Collection<EObject> getSourceObjects(String sourceId_p) {
    return sourceMap.get(sourceId_p);
  }

  public EObject getTracedObject(String sourceId_p) {
    Collection<EObject> objects = getTracedObjects(sourceId_p);
    if (!objects.isEmpty()) {
      return objects.iterator().next();
    }
    return null;
  }

  public EObject getTracedObject(EObject source) {
    Collection<EObject> objects = getTracedObjects(getSourceId(source));
    if (!objects.isEmpty()) {
      return objects.iterator().next();
    }
    return null;
  }

  public EObject getSourceObject(EObject target) {
    Collection<EObject> objects = getSourceObjects(getSourceId(target));
    if (!objects.isEmpty()) {
      return objects.iterator().next();
    }
    return null;
  }

}
