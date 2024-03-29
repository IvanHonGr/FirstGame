#ifndef Sprite_H
#define Sprite_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/entity/shape/RectangularShape.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/entity/sprite/vbo/ISpriteVertexBufferObject.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/entity/sprite/vbo/ISpriteVertexBufferObject.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/entity/sprite/vbo/ISpriteVertexBufferObject.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/entity/sprite/vbo/ISpriteVertexBufferObject.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/input/touch/TouchEvent.h"
#include "src/org/andengine/input/touch/TouchEvent.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeInitClass(JNIEnv*, jclass);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnAreaTouched(JNIEnv*, jobject, jlong, jobject, jfloat, jfloat);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnAttached(JNIEnv*, jobject, jlong);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnDetached(JNIEnv*, jobject, jlong);
}

class Sprite : public RectangularShape {

	public:
		Sprite(jobject);
		virtual jobject unwrap();
		Sprite(jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*);
		Sprite(jfloat, jfloat, ITextureRegion*, ISpriteVertexBufferObject*, ShaderProgram*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, ShaderProgram*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, DrawType*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, DrawType*, ShaderProgram*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, ISpriteVertexBufferObject*);
		Sprite(jfloat, jfloat, jfloat, jfloat, ITextureRegion*, ISpriteVertexBufferObject*, ShaderProgram*);
		Sprite(jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, ShaderProgram*);
		Sprite(jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, DrawType*);
		Sprite(jfloat, jfloat, ITextureRegion*, VertexBufferObjectManager*, DrawType*, ShaderProgram*);
		Sprite(jfloat, jfloat, ITextureRegion*, ISpriteVertexBufferObject*);
		Sprite();
		virtual jfloat getWidth();
		virtual jfloat getHeight();
		virtual VertexBufferObjectManager* getVertexBufferObjectManager();
		virtual jboolean onAreaTouched(TouchEvent*, jfloat, jfloat);
		virtual jfloat getY();
		virtual jfloat getX();
		virtual void setColor(jfloat, jfloat, jfloat);
		virtual void setColor(Color*);
		virtual void setColor(jfloat, jfloat, jfloat, jfloat);
		virtual void setScale(jfloat, jfloat);
		virtual void setScale(jfloat);
		virtual jfloat getRotation();
		virtual void setRotation(jfloat);
		virtual void attachChild(IEntity*);
		virtual jboolean attachChild(IEntity*, jint);
		virtual void setX(jfloat);
		virtual void setY(jfloat);
		virtual jfloat getScaleX();
		virtual jfloat getScaleY();
		virtual jfloat getSkewX();
		virtual jfloat getSkewY();
		virtual void setSkew(jfloat, jfloat);
		virtual void setSkew(jfloat);
		virtual jfloat getRed();
		virtual jfloat getGreen();
		virtual jfloat getBlue();
		virtual jfloat getAlpha();
		virtual Color* getColor();
		virtual void setRed(jfloat);
		virtual void setGreen(jfloat);
		virtual void setBlue(jfloat);
		virtual void setAlpha(jfloat);
		virtual jint getChildCount();
		virtual IEntity* getChild(jint);
		virtual IEntity* getChild(IEntityMatcher*);
		virtual jboolean detachSelf();
		virtual IEntity* detachChild(IEntityMatcher*);
		virtual jboolean detachChild(IEntity*);
		virtual jboolean onAttached();
		virtual jboolean swapChildren(jint, jint);
		virtual jboolean swapChildren(IEntity*, IEntity*);
		virtual jboolean onDetached();

	protected:

	private:

};
#endif

