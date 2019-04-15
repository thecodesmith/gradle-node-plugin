package com.moowork.gradle.node.npm

import com.moowork.gradle.node.exec.ExecRunner
import org.gradle.api.Project
import org.gradle.process.ExecResult

class NpmExecRunner
    extends ExecRunner
{
    NpmExecRunner( final Project project )
    {
        super( project )
    }

    @Override
    protected ExecResult doExecute()
    {
        def exec = variant.npmExec

        if ( ext.download )
        {
            def separator = File.pathSeparator
            def npmBinDir = variant.npmBinDir.absolutePath
            def nodeBinDir = variant.nodeBinDir.absolutePath
            def nodeModulesBinDir = ext.nodeModulesDir.absolutePath + '/node_modules/.bin'

            def path = npmBinDir + separator + nodeBinDir + separator + nodeModulesBinDir

            // Take care of Windows environments that may contain "Path" OR "PATH" - both existing
            // possibly (but not in parallel as of now)
            if ( environment['Path'] != null )
            {
                environment.put('Path', path + separator + environment['Path'])
            }
            else
            {
                environment.put('PATH', path + separator + environment['PATH'])
            }

            File localNpm = project.file( new File( ext.nodeModulesDir, 'node_modules/npm/bin/npm-cli.js' ) )
            if ( localNpm.exists() )
            {
                exec = variant.nodeExec
                arguments = [localNpm.absolutePath] + arguments
            }
            else if ( !new File(exec).exists() )
            {
                exec = variant.nodeExec
                arguments = [variant.npmScriptFile] + arguments
            }
        }
        return run( exec, arguments )
    }
}
