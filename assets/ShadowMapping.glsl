#type vertex
#version 410
layout (location = 0) in vec3 aPos;
uniform mat4 lightProjection;
uniform mat4 lightView;
uniform mat4 modelMatrix;

void main() {
    gl_Position = ((lightProjection * lightView) * modelMatrix) *   vec4(aPos, 1.0);
}

#type fragment
#version 410
void main() {
}