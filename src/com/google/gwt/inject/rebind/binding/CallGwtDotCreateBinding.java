/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.inject.rebind.binding;

import com.google.gwt.inject.rebind.util.KeyUtil;
import com.google.gwt.inject.rebind.util.SourceWriteUtil;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.inject.Inject;

/**
 * A binding that just calls {@code GWT.create()} for the requested type.
 * This is the default binding for interfaces or classes that don't have
 * a non-default constructor annotated with {@code @Inject}.
 */
public class CallGwtDotCreateBinding extends CreatorBinding {

  @Inject
  public CallGwtDotCreateBinding(SourceWriteUtil sourceWriteUtil, KeyUtil keyUtil) {
    super(sourceWriteUtil, keyUtil);
  }

  @Override protected final void appendCreationStatement(SourceWriter sourceWriter,
      StringBuilder sb) {

    String name = getTypeNameToCreate();
    sb.append("Object created = ");
    sb.append("GWT.create(")
        .append(name)
        .append(".class);\n");

    // Gin cannot deal with cases where the type returned by GWT.create is not
    // equal or a subtype of the requested type. Check for this and throw an
    // exception if necessary:
    sb.append(getTypeName()).append(" result;\n")
        .append("if (!(created instanceof ").append(getTypeName()).append(")) {\n")
        .append("  throw new com.google.gwt.inject.client.CreationException(\"GWT.create returned")
        .append(" a type incompatible to the one requested: Received \" + created.getClass() + \",")
        .append(" expected ").append(getTypeName()).append(".\");\n")
        .append("} else {\n")
        .append("  result = (").append(getTypeName()).append(") created;\n")
        .append("}");
  }

  protected String getTypeNameToCreate() {
    return getClassType().getQualifiedSourceName();
  }
}